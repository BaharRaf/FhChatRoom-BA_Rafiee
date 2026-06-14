/**
 * Security-rules unit tests for FhChatRoom (Firestore + Storage).
 *
 * Proves the behaviour of firestore.rules / storage.rules against the local
 * emulator -- no live project, no credentials. Run with:
 *
 *   cd firebase-tests && npm install && npm test
 *
 * Each test asserts an allow OR a deny, covering the scenarios the reviews
 * raised: member reactions, non-author content edits, room field allowlist,
 * owner control, self-join, recommendation-field protection, academic
 * template re-sync, student-domain gating, system-room minting, friend
 * scoping, and chat-media upload guards.
 */

const fs = require("fs");
const path = require("path");
const assert = require("assert");
const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const {
  doc, getDoc, setDoc, updateDoc, deleteDoc,
} = require("firebase/firestore");
const { ref, uploadBytes } = require("firebase/storage");

const PROJECT_ID = "demo-fhchatroom";
const ALICE = "alice@stud.hcw.ac.at";
const BOB = "bob@stud.hcw.ac.at";
const CAROL = "carol@stud.hcw.ac.at";
const OUTSIDER = "mallory@gmail.com";

let env;

function db(email) {
  // authenticate with an email-verified token carrying the email claim
  return env.authenticatedContext(email.replace(/[^a-z]/g, ""), { email, email_verified: true })
    .firestore();
}
function storageAs(email) {
  return env.authenticatedContext(email.replace(/[^a-z]/g, ""), { email, email_verified: true })
    .storage();
}

// Seed data bypassing rules.
async function seed() {
  await env.withSecurityRulesDisabled(async (ctx) => {
    const f = ctx.firestore();
    await setDoc(doc(f, "users", ALICE), { email: ALICE, recommendedRoomIds: ["x"], recommendationSource: "GRAPH_SAGE_LOCAL" });
    await setDoc(doc(f, "rooms", "pub"), {
      id: "pub", name: "Algorithms", ownerEmail: ALICE,
      members: [ALICE], isPrivate: false, isDirect: false, templateRoom: false,
    });
    await setDoc(doc(f, "rooms", "tmpl"), {
      id: "tmpl", name: "CS Sem 1", ownerEmail: "system",
      members: [ALICE], isPrivate: false, isDirect: false, templateRoom: true,
    });
    await setDoc(doc(f, "rooms", "pub", "messages", "m1"), {
      senderId: ALICE, text: "hi", reactions: {}, deletedFor: [],
    });
    await setDoc(doc(f, "friendRequests", "r1"), { fromEmail: ALICE, toEmail: BOB });
  });
}

before(async () => {
  env = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: { rules: fs.readFileSync(path.resolve(__dirname, "../firestore.rules"), "utf8") },
    storage: { rules: fs.readFileSync(path.resolve(__dirname, "../storage.rules"), "utf8") },
  });
});
after(async () => { await env.cleanup(); });
beforeEach(async () => { await env.clearFirestore(); await seed(); });

describe("users", () => {
  it("student can create their own user doc", async () => {
    await assertSucceeds(setDoc(doc(db(CAROL), "users", CAROL), { email: CAROL }));
  });
  it("non-student domain cannot create a user doc", async () => {
    await assertFails(setDoc(doc(db(OUTSIDER), "users", OUTSIDER), { email: OUTSIDER }));
  });
  it("owner cannot self-write recommendation fields", async () => {
    await assertFails(updateDoc(doc(db(ALICE), "users", ALICE), { recommendedRoomIds: ["evil"] }));
  });
  it("owner can update non-recommendation profile fields", async () => {
    await assertSucceeds(updateDoc(doc(db(ALICE), "users", ALICE), { studyPath: "CS" }));
  });
});

describe("rooms", () => {
  it("non-member can self-join a public room", async () => {
    await assertSucceeds(updateDoc(doc(db(BOB), "rooms", "pub"), { members: [ALICE, BOB] }));
  });
  it("non-member cannot edit room content via fake self-join", async () => {
    await assertFails(updateDoc(doc(db(BOB), "rooms", "pub"), { members: [ALICE, BOB], name: "hijack" }));
  });
  it("member can update an allowlisted field (lastMessage)", async () => {
    await env.withSecurityRulesDisabled(async (ctx) =>
      updateDoc(doc(ctx.firestore(), "rooms", "pub"), { members: [ALICE, BOB] }));
    await assertSucceeds(updateDoc(doc(db(BOB), "rooms", "pub"), { lastMessage: "hello" }));
  });
  it("non-owner member cannot change visibility", async () => {
    await env.withSecurityRulesDisabled(async (ctx) =>
      updateDoc(doc(ctx.firestore(), "rooms", "pub"), { members: [ALICE, BOB] }));
    await assertFails(updateDoc(doc(db(BOB), "rooms", "pub"), { isPrivate: true }));
  });
  it("owner can change visibility of their room", async () => {
    await assertSucceeds(updateDoc(doc(db(ALICE), "rooms", "pub"), { isPrivate: true }));
  });
  it("user cannot mint an arbitrary system-owned room", async () => {
    await assertFails(setDoc(doc(db(BOB), "rooms", "fake"), { ownerEmail: "system", members: [BOB] }));
  });
  it("user can create a valid public template room", async () => {
    await assertSucceeds(setDoc(doc(db(BOB), "rooms", "tmpl2"), {
      ownerEmail: "system", members: [BOB], templateRoom: true, isPrivate: false, isDirect: false,
    }));
  });
});

describe("messages", () => {
  it("member can react to another user's message", async () => {
    await env.withSecurityRulesDisabled(async (ctx) =>
      updateDoc(doc(ctx.firestore(), "rooms", "pub"), { members: [ALICE, BOB] }));
    await assertSucceeds(updateDoc(doc(db(BOB), "rooms", "pub", "messages", "m1"),
      { reactions: { [BOB]: "👍" } }));
  });
  it("member can hide another user's message for themselves", async () => {
    await env.withSecurityRulesDisabled(async (ctx) =>
      updateDoc(doc(ctx.firestore(), "rooms", "pub"), { members: [ALICE, BOB] }));
    await assertSucceeds(updateDoc(doc(db(BOB), "rooms", "pub", "messages", "m1"),
      { deletedFor: [BOB] }));
  });
  it("member cannot edit another user's message text", async () => {
    await env.withSecurityRulesDisabled(async (ctx) =>
      updateDoc(doc(ctx.firestore(), "rooms", "pub"), { members: [ALICE, BOB] }));
    await assertFails(updateDoc(doc(db(BOB), "rooms", "pub", "messages", "m1"),
      { text: "tampered" }));
  });
  it("author can edit their own message text", async () => {
    await assertSucceeds(updateDoc(doc(db(ALICE), "rooms", "pub", "messages", "m1"),
      { text: "edited" }));
  });
});

describe("friends", () => {
  it("involved party can read a friend request", async () => {
    await assertSucceeds(getDoc(doc(db(BOB), "friendRequests", "r1")));
  });
  it("uninvolved user cannot read a friend request", async () => {
    await assertFails(getDoc(doc(db(CAROL), "friendRequests", "r1")));
  });
});

describe("storage", () => {
  const bytes = new Uint8Array([1, 2, 3]);
  it("signed-in user can upload a chat image with image content type", async () => {
    await assertSucceeds(uploadBytes(ref(storageAs(BOB), "images/x.jpg"), bytes,
      { contentType: "image/jpeg" }));
  });
  it("signed-in user can upload a voice note with audio content type", async () => {
    await assertSucceeds(uploadBytes(ref(storageAs(BOB), "voice/x.3gp"), bytes,
      { contentType: "audio/3gpp" }));
  });
  it("a non-image upload to images/ is denied", async () => {
    await assertFails(uploadBytes(ref(storageAs(BOB), "images/x.exe"), bytes,
      { contentType: "application/octet-stream" }));
  });
  it("upload to an unknown path is denied", async () => {
    await assertFails(uploadBytes(ref(storageAs(BOB), "secrets/x.txt"), bytes,
      { contentType: "text/plain" }));
  });
});
