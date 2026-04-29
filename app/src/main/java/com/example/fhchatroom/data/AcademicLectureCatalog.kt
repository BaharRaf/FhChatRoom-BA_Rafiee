package com.example.fhchatroom.data

// Generated from the public HCW study offer pages for the academic-room prototype.
// Runtime stays offline: the app reads this static catalog and never scrapes the website.
internal val lectureCatalogByStudyPath: Map<String, Map<Long, List<String>>> by lazy {
    parseAcademicLectureCatalog(academicLectureCatalogRows())
}

private fun academicLectureCatalogRows(): String = buildString {
        append(
            """
Advanced Manufacturing Technologies and Management	1	Einsatz von KI/ML in der Produktion | ILV
Advanced Manufacturing Technologies and Management	1	Business English for Experts | ILV
Advanced Manufacturing Technologies and Management	1	Intercultural Communication in English | ILV
Advanced Manufacturing Technologies and Management	1	Managementsysteme zur Unternehmensführung | ILV
Advanced Manufacturing Technologies and Management	1	Computational Fluid Dynamics | ILV
Advanced Manufacturing Technologies and Management	1	Produktionsqualitätssicherungssysteme | VO
Advanced Manufacturing Technologies and Management	1	Werkzeugmaschinen | ILV
Advanced Manufacturing Technologies and Management	1	Lasertechnik in der Fertigung | ILV
Advanced Manufacturing Technologies and Management	1	Technologie der Fertigungsverfahren | ILV
Advanced Manufacturing Technologies and Management	1	Werkstoffeigenschaften | VO
Advanced Manufacturing Technologies and Management	2	Additive Manufacturing Technologies - Einführung und Grundlagen | ILV
Advanced Manufacturing Technologies and Management	2	Sustainable Design for Additive Manufacturing | ILV
Advanced Manufacturing Technologies and Management	2	Deep Learning in der Produktion | ILV
Advanced Manufacturing Technologies and Management	2	Marketing und Vertrieb | VO
Advanced Manufacturing Technologies and Management	2	Dispositionserstellung und wissenschaftliches Arbeiten | SE
Advanced Manufacturing Technologies and Management	2	Logistik in der produzierenden Industrie | VO
Advanced Manufacturing Technologies and Management	2	Simulation technischer Systeme 1 | ILV
Advanced Manufacturing Technologies and Management	2	Präzisions- und Mikrozerspanung | VO
Advanced Manufacturing Technologies and Management	2	Qualitätsstandards und Anwendungen in der Produktion | ILV
Advanced Manufacturing Technologies and Management	2	High-Tech Materials | ILV
Advanced Manufacturing Technologies and Management	2	Photonische Bearbeitungsverfahren | ILV
Advanced Manufacturing Technologies and Management	2	Werkstoffeinsatz | ILV
Advanced Manufacturing Technologies and Management	3	Interdisziplinäres Projekt in Additive Manufacturing | SE
Advanced Manufacturing Technologies and Management	3	Masterarbeitsseminar | SE
Advanced Manufacturing Technologies and Management	3	Produktionssteuerungssysteme | ILV
Advanced Manufacturing Technologies and Management	3	Aktuelle Beispiele aus High Tech Manufacturing | VO
Advanced Manufacturing Technologies and Management	3	Mechanische Verfahrenstechnik und Verfahren in der Produktion | VO
Advanced Manufacturing Technologies and Management	3	Thermische und chemische Verfahrenstechnik in der Industrie | VO
Advanced Manufacturing Technologies and Management	3	Simulation technischer Systeme 2 | ILV
Advanced Manufacturing Technologies and Management	3	Simulation von Fertigungsanlagen | ILV
Advanced Manufacturing Technologies and Management	3	Entrepreneurship | ILV
Advanced Manufacturing Technologies and Management	3	Internationalisation of Digital Fabrication | SE
Advanced Manufacturing Technologies and Management	3	Prozessmodellierung | ILV
Advanced Manufacturing Technologies and Management	4	Master Thesis – Erstellen der Masterarbeit | UE
Advanced Manufacturing Technologies and Management	4	Master Thesis – Masterprüfung | SE
Advanced Manufacturing Technologies and Management	4	Messtechnik | VO
Advanced Manufacturing Technologies and Management	4	Testkonzepte und Testsysteme | ILV
Advanced Manufacturing Technologies and Management	4	Virtuelle Verifikation von Fertigungsprozessen | ILV
Advanced Manufacturing Technologies and Management	4	Unternehmenssteuerung und Produktionscontrolling | ILV
Advanced Nursing Counseling	1	Angewandte Advanced Nursing Practice | ILV
Advanced Nursing Counseling	1	Ethik und ethisches Handeln in der Pflegepraxis | ILV
Advanced Nursing Counseling	1	Reflexive Kompetenzentwicklung | ILV
Advanced Nursing Counseling	1	Klinisches Assessment | ILV
Advanced Nursing Counseling	1	Pathologie/Pathophysiologie | ILV
Advanced Nursing Counseling	1	Pharmakologie | ILV
Advanced Nursing Counseling	1	Beratungsmethoden und Beratungsprozesse | ILV
Advanced Nursing Counseling	1	Beratungstheorien und -konzepte | ILV
Advanced Nursing Counseling	1	Fachenglisch und Englische Fachliteraturarbeit | ILV
Advanced Nursing Counseling	1	Vertiefung Pflegewissenschaft | ILV
Advanced Nursing Counseling	1	Wissenschaftliches Arbeiten | ILV
Advanced Nursing Counseling	1	Evidencebasierte Pflege von Erwachsenen | VO
Advanced Nursing Counseling	1	Evidencebasierte Pflege von Kindern und Jugendlichen | VO
Advanced Nursing Counseling	1	Public Health | VO
Advanced Nursing Counseling	2	Inhaltsorientierte Gesundheits- und Pflegeberatung | ILV
Advanced Nursing Counseling	2	Settingorientierte Gesundheits- und Pflegeberatung | ILV
Advanced Nursing Counseling	2	Beratung in Gruppen und sozialen Systemen | ILV
Advanced Nursing Counseling	2	Beratungssimulation | UE
Advanced Nursing Counseling	2	Schnittstellenmanagement in der Pflege | ILV
Advanced Nursing Counseling	2	Beratungspraxis und Reflexion | PR
Advanced Nursing Counseling	2	Grundlagen der Kommunikation in Counseling | ILV
Advanced Nursing Counseling	2	Rhetorik | UE
Advanced Nursing Counseling	2	Angewandte Pflegeforschung | ILV
Advanced Nursing Counseling	2	Qualitative Methoden | ILV
Advanced Nursing Counseling	2	Quantitative Methoden | ILV
Advanced Nursing Counseling	3	Case- und Caremanagement - Grundlagen und Fallebene | ILV
Advanced Nursing Counseling	3	Case- und Caremanagement auf Systemebene und Evaluation | ILV
Advanced Nursing Counseling	3	Forschungsbasierte Pflegepraxis | ILV
Advanced Nursing Counseling	3	Statistik | ILV
Advanced Nursing Counseling	3	Internationalisation of Counseling | ILV
Advanced Nursing Counseling	3	Projekt- und Qualitätsmanagement | SE
Advanced Nursing Counseling	3	Beratungsprofessionalisierung | ILV
Advanced Nursing Counseling	3	Fallorientierte Gesundheits- und Pflegeberatung | ILV
Advanced Nursing Counseling	3	Schulungs- und Vermittlungskonzepte in der Pflegepraxis | ILV
Advanced Nursing Counseling	3	Counseling Skill & Sim Lab | ILV
Advanced Nursing Counseling	3	Personenbezogene Education, Counseling, Kommunikation | ILV
Advanced Nursing Counseling	4	Masterarbeit
Advanced Nursing Counseling	4	Masterarbeit Begleitseminar | SE
Advanced Nursing Counseling	4	Masterthesis Examination | AP
Advanced Nursing Counseling	4	Digitale Gesundheitskompetenz | ILV
Advanced Nursing Counseling	4	Rechtsgrundlagen in der Pflegeberatung | UE
Advanced Nursing Counseling	4	Sozialsystem und Selbständigkeit | ILV
Advanced Nursing Counseling	4	Strukturen, Einrichtungen und Finanzierung des Gesundheitswesens | VO
Advanced Nursing Education	1	Angewandte Advanced Nursing Practice | ILV
Advanced Nursing Education	1	Ethik und ethisches Handeln in d. Pflegepraxis | ILV
Advanced Nursing Education	1	Reflexive Kompetenzentwicklung | ILV
Advanced Nursing Education	1	Lehr- u. Lernprozessgestaltung, einschl. Training 1 | ILV
Advanced Nursing Education	1	Methodische Konzeption des Unterrichts 1 | ILV
Advanced Nursing Education	1	Bildungswissenschaft und Bildungstheorien | ILV
Advanced Nursing Education	1	Didaktische Konzepte/ Hochschuldidaktik | ILV
Advanced Nursing Education	1	Fachenglisch und Englische Fachliteraturarbeit | ILV
Advanced Nursing Education	1	Vertiefung Pflegewissenschaft | ILV
Advanced Nursing Education	1	Wissenschaftliches Arbeiten | ILV
Advanced Nursing Education	1	Evidencebasierte Pflege von Erwachsenen | VO
Advanced Nursing Education	1	Evidencebasierte Pflege von Kindern und Jugendlichen | VO
Advanced Nursing Education	1	Public Health | VO
Advanced Nursing Education	2	Lehr- u. Lernprozessgestaltung, einschl. Training 2 | ILV
Advanced Nursing Education	2	Leistungsfeststellung und -beurteilung | ILV
Advanced Nursing Education	2	Grundlagen der Kommunikation in Education | ILV
Advanced Nursing Education	2	Lehrpraxis und Reflexion | PR
Advanced Nursing Education	2	Methodische Konzeption des Unterrichts 2 | ILV
Advanced Nursing Education	2	Rhetorik | UE
Advanced Nursing Education	2	Bildungsmanagement | ILV
Advanced Nursing Education	2	Angewandte Pflegeforschung | ILV
Advanced Nursing Education	2	Qualitative Methoden | ILV
Advanced Nursing Education	2	Quantitative Methoden | ILV
Advanced Nursing Education	3	Forschungsbasierte Pflegepraxis | ILV
Advanced Nursing Education	3	Statistik | ILV
Advanced Nursing Education	3	Projekt- und Qualitätsmanagement | SE
Advanced Nursing Education	3	Mediendidaktik und Medienpädagogik | ILV
Advanced Nursing Education	3	Bildungspolitik und Hochschulrecht | ILV
Advanced Nursing Education	3	Curricula- und Lehrplanentwicklung | SE
Advanced Nursing Education	3	Coaching, Beratung und Krisenmanagement | SE
Advanced Nursing Education	3	Lehrenden Professionalisierung | ILV
Advanced Nursing Education	3	Education Skill & Sim Lab | ILV
Advanced Nursing Education	3	Personenbezogene Education, Counseling, Kommunikation | ILV
Advanced Nursing Education	4	Internationalisation of learning and teaching | ILV
Advanced Nursing Education	4	Masterarbeit
Advanced Nursing Education	4	Masterthesis Begleitseminar | SE
Advanced Nursing Education	4	Masterthesis Examination | AP
Advanced Nursing Education	4	E-Learning und E-Teaching | SE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Angewandte Advanced Nursing Practice | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Ethik und ethisches Handeln in d. Pflegepraxis | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Reflexive Kompetenzentwicklung | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Controlling | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Finanz- und Rechnungswesen | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Grundlagen Leadership und Organisation | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Grundlagen Personalmanagement | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Fachenglisch und Englische Fachliteraturarbeit | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Vertiefung Pflegewissenschaft | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Wissenschaftliches Arbeiten | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Evidencebasierte Pflege von Erwachsenen | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Evidencebasierte Pflege von Kindern und Jugendlichen | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	1	Public Health | VO
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Leadership Simulation | UE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Verhandlungsführung, Delegation und Subdelegation | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Wissens- und Changemanagement | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Grundlagen der Kommunikation in Management | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Managementpraxis und Reflexion | PR
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Rhetorik | UE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Angewandte Pflegeforschung | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Qualitative Methoden | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Quantitative Methoden | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Angewandtes Pflegemanagement | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Case- und Caremanagement | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	2	Personalentwicklung | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Forschungsbasierte Pflegepraxis | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Statistik | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Internationalisation of management and leadership | SE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Projekt- und Qualitätsmanagement | SE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Krisen- und Risikomanagement | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Management Professionalisierung | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Öffentlichkeitsarbeit und Marketing | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Management Skill & Sim Lab | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	3	Personenbezogene Education, Counseling, Kommunikation | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Masterarbeit
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Masterthesis Begleitseminar | SE
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Masterthesis Examination | AP
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Gesundheitsökonomie | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Organisationsformen in Gesundheits- und Sozialsystemen | ILV
Advanced Nursing Practice – Schwerpunkt Pflegemanagement	4	Rechtsgrundlagen für Gesundheitsberufe | UE
Angewandte Elektronik und Technische Informatik	1	Mathematik 1 | ILV
Angewandte Elektronik und Technische Informatik	1	Basics of Business English | UE
Angewandte Elektronik und Technische Informatik	1	Digitaltechnik | ILV
Angewandte Elektronik und Technische Informatik	1	Elektronik-Laboratorium 1 | UE
Angewandte Elektronik und Technische Informatik	1	Grundlagen der Elektrotechnik 1 | ILV
Angewandte Elektronik und Technische Informatik	1	Lernstrategien und Arbeitsmethoden 1 | ILV
Angewandte Elektronik und Technische Informatik	1	C-Programmierung | ILV
Angewandte Elektronik und Technische Informatik	2	Mathematik 2 | ILV
Angewandte Elektronik und Technische Informatik	2	Intermediate Business English | UE
Angewandte Elektronik und Technische Informatik	2	Digitale Systeme | ILV
Angewandte Elektronik und Technische Informatik	2	Elektronik-Laboratorium 2 | UE
Angewandte Elektronik und Technische Informatik	2	Grundlagen der Elektrotechnik 2 | ILV
Angewandte Elektronik und Technische Informatik	2	Lernstrategien und Arbeitsmethoden 2 | ILV
Angewandte Elektronik und Technische Informatik	2	Physik und Sensoren 1 | ILV
Angewandte Elektronik und Technische Informatik	2	Fortgeschrittene C-Programmierung | VO
Angewandte Elektronik und Technische Informatik	2	Fortgeschrittene C-Programmierung | UE
Angewandte Elektronik und Technische Informatik	3	Programmieren von Mikrocontrollern | VO
Angewandte Elektronik und Technische Informatik	3	Programmieren von Mikrocontrollern | UE
Angewandte Elektronik und Technische Informatik	3	Elektrische Messtechnik | ILV
Angewandte Elektronik und Technische Informatik	3	Physik und Sensoren 2 | ILV
Angewandte Elektronik und Technische Informatik	3	Mathematische Methoden der Elektrotechnik | ILV
Angewandte Elektronik und Technische Informatik	3	Regelungstechnik | ILV
Angewandte Elektronik und Technische Informatik	3	Angewandte Schaltungstechnik | ILV
Angewandte Elektronik und Technische Informatik	4	Angewandte Mikrocontrollerprogrammierung | UE
Angewandte Elektronik und Technische Informatik	4	Technical English 1 | UE
Angewandte Elektronik und Technische Informatik	4	Wissenschaftliches Arbeiten | VO
Angewandte Elektronik und Technische Informatik	4	Elektronischer Geräteentwurf 1 | ILV
Angewandte Elektronik und Technische Informatik	4	Aktoren | VO
Angewandte Elektronik und Technische Informatik	4	Automatisierung techn. Prozesse | ILV
Angewandte Elektronik und Technische Informatik	4	Leistungselektronik | ILV
Angewandte Elektronik und Technische Informatik	4	Photonik und Optoelektronik | VO
Angewandte Elektronik und Technische Informatik	4	Schaltungs- und Systementwurf | UE
Angewandte Elektronik und Technische Informatik	4	Schaltungstechnik-Laboratorium | UE
Angewandte Elektronik und Technische Informatik	5	Technical English 2 | UE
Angewandte Elektronik und Technische Informatik	5	Antriebssysteme | VO
Angewandte Elektronik und Technische Informatik	5	Elektrische Energiespeicher | VO
Angewandte Elektronik und Technische Informatik	5	Ausgewählte Kapitel der Informatik | SE
Angewandte Elektronik und Technische Informatik	5	Elektronischer Geräteentwurf 2 | UE
Angewandte Elektronik und Technische Informatik	5	Projektmanagement | ILV
Angewandte Elektronik und Technische Informatik	5	Wirtschaft | ILV
Angewandte Elektronik und Technische Informatik	5	Automatisierung techn. Prozesse 2 | ILV
Angewandte Elektronik und Technische Informatik	5	SPS Systeme und Steuerungssysteme | ILV
Angewandte Elektronik und Technische Informatik	5	Energieeffizienz und Klimaschutzstrategien | ILV
Angewandte Elektronik und Technische Informatik	5	Umweltschutz in der Produktion | ILV
Angewandte Elektronik und Technische Informatik	5	Grundlagen erneuerbarer Energien | ILV
Angewandte Elektronik und Technische Informatik	5	Ökodesign | VO
Angewandte Elektronik und Technische Informatik	6	Elektromobilität | VO
Angewandte Elektronik und Technische Informatik	6	Bachelorarbeit
Angewandte Elektronik und Technische Informatik	6	Bachelorarbeit | SE
Angewandte Elektronik und Technische Informatik	6	Berufspraktikum
Angewandte Elektronik und Technische Informatik	6	Berufspraktikum | PR
Angewandte Elektronik und Technische Informatik	6	Ausgewählte Kapitel der Elektronik | SE
Angewandte Elektronik und Technische Informatik	6	Product Life Cycle Management | VO
Angewandte Elektronik und Technische Informatik	6	Privat- und Patentrecht | VO
Angewandte Elektronik und Technische Informatik	6	Human Machine Interface | ILV
Angewandte Elektronik und Technische Informatik	6	Prozessleitsysteme und Feldbustechnik | ILV
Angewandte Elektronik und Technische Informatik	6	Technologien zur Nutzung erneuerbarer Energien | ILV
Angewandte Elektronik und Technische Informatik	6	Umweltmesstechnik | ILV
Architektur – Green Building	1	CAD | UE
Architektur – Green Building	1	Darstellende Geometrie 1 | VO
Architektur – Green Building	1	Freihandzeichnen und Skizzieren 1 | UE
Architektur – Green Building	1	Modellbau | UE
Architektur – Green Building	1	Visualisierung | VO
Architektur – Green Building	1	Gebäudephysik 1 | VO
Architektur – Green Building	1	Entwerfen 1 | UE
Architektur – Green Building	1	Gestalten und Entwerfen 1 | ILV
Architektur – Green Building	1	Baukonstruktion 1 | ILV
Architektur – Green Building	1	Plandarstellung und bautechnisches Zeichnen | ILV
Architektur – Green Building	1	Baumaterialien | ILV
Architektur – Green Building	1	Baumaterialien und Green Building | VO
Architektur – Green Building	1	Einführung in Green Building | VO
Architektur – Green Building	1	Öffentlich rechtliche Grundlagen | VO
Architektur – Green Building	1	Statik und Festigkeitslehre 1 | VO
Architektur – Green Building	2	Darstellende Geometrie 2 | VO
Architektur – Green Building	2	Freihandzeichnen und Skizzieren 2 | UE
Architektur – Green Building	2	Innovative Energiekonzepte 1 | VO
Architektur – Green Building	2	Entwerfen 2 | UE
Architektur – Green Building	2	Gestalten und Entwerfen 2 | VO
Architektur – Green Building	2	Baukonstruktion 2 | VO
Architektur – Green Building	2	Baukonstruktion I | UE
Architektur – Green Building	2	Bau- und Raumordnungsrecht | VO
Architektur – Green Building	2	Baumanagement | VO
Architektur – Green Building	2	Stahl- und Holzbau | VO
Architektur – Green Building	2	Stahlbetonbau | VO
Architektur – Green Building	2	Statik und Festigkeitslehre 2 | ILV
Architektur – Green Building	3	Architektur- und Kunstgeschichte | VO
Architektur – Green Building	3	Architekturtheorie | VO
Architektur – Green Building	3	Einführung ins wissenschaftliche Arbeiten | SE
Architektur – Green Building	3	Gebäudephysik 2 | VO
Architektur – Green Building	3	Wasser und Gebäude | VO
Architektur – Green Building	3	Entwerfen 3 | UE
Architektur – Green Building	3	Gestalten und Entwerfen 3 | VO
Architektur – Green Building	3	Klimagerechtes Bauen und Entwerfen | VO
Architektur – Green Building	3	Klimagerechtes Bauen und Entwerfen | UE
Architektur – Green Building	3	Baukonstruktion 3 | VO
Architektur – Green Building	3	Baukonstruktion II | UE
Architektur – Green Building	3	Städtebau und Raumordnung | VO
Architektur – Green Building	3	Wohnbau | VO
Architektur – Green Building	4	Soziologie | VO
Architektur – Green Building	4	Workshop: Wissenschaftliches Arbeiten | UE
Architektur – Green Building	4	Gebäudeautomation – Smart Building | VO
Architektur – Green Building	4	Innovative Energiekonzepte | UE
Architektur – Green Building	4	Innovative Energiekonzepte 2 | VO
Architektur – Green Building	4	Entwerfen 4 | UE
Architektur – Green Building	4	Gestalten und Entwerfen 4 | VO
Architektur – Green Building	4	Parametrisches Design | UE
Architektur – Green Building	4	Plastisches Gestalten | UE
Architektur – Green Building	4	Berufspraktikum | PR
Architektur – Green Building	4	Seminar zum Berufspraktikum | SE
Architektur – Green Building	4	Baukonstruktion 4 | VO
Architektur – Green Building	5	Life-Cycle-Management | VO
Architektur – Green Building	5	Entwerfen 5 | UE
Architektur – Green Building	5	Gestalten und Entwerfen 5 | VO
Architektur – Green Building	5	Lichttechnik | VO
Architektur – Green Building	5	Nachhaltigkeit von Bauteilen und Konstruktionen | VO
Architektur – Green Building	5	Soziale Nachhaltigkeit | ILV
Architektur – Green Building	5	Tageslichtarchitektur | VO
Architektur – Green Building	5	Gebäudelehre | VO
Architektur – Green Building	5	Baukonstruktion 5 | VO
Architektur – Green Building	5	Baukonstruktion III | UE
Architektur – Green Building	5	Generatives Design | UE
Architektur – Green Building	5	Konzeptioneller Brandschutz | VO
Architektur – Green Building	5	Planen und Bauen im Bestand | VO
Architektur – Green Building	6	Ökologie in der Ausschreibung, Vergabe und Abrechnung | VO
Architektur – Green Building	6	Projektmanagement | VO
Architektur – Green Building	6	Entwerfen 6 | UE
Architektur – Green Building	6	Integrale Planung (= Bachelorarbeit) | ILV
Architektur – Green Building	6	Gebäudezertifizierungssysteme | VO
Architektur – Green Building	6	Innenraumanalytik | VO
Architektur – Green Building	6	Freiraumplanung | VO
Architektur – Green Building	6	Freiraumplanung | UE
Bauingenieurwesen – Baumanagement	1	Baukonstruktion 1 | ILV
Bauingenieurwesen – Baumanagement	1	Plandarstellung und bautechnisches Zeichnen | UE
Bauingenieurwesen – Baumanagement	1	Nachhaltige Baustoffkunde und Labor 1 | ILV
Bauingenieurwesen – Baumanagement	1	Basics of Business English | VO
Bauingenieurwesen – Baumanagement	1	Betriebswirtschaftslehre für den Baubetrieb 1 | VO
Bauingenieurwesen – Baumanagement	1	Lernmethoden und Lernstrategien | UE
Bauingenieurwesen – Baumanagement	1	Geotechnik 1 | VO
Bauingenieurwesen – Baumanagement	1	CAD | UE
Bauingenieurwesen – Baumanagement	1	Mathematik für Bautechnik | ILV
Bauingenieurwesen – Baumanagement	1	Physik und Bauphysik | VO
Bauingenieurwesen – Baumanagement	1	Öffentlich rechtliche Grundlagen | VO
Bauingenieurwesen – Baumanagement	1	Statik und Festigkeitslehre 1 | VO
Bauingenieurwesen – Baumanagement	1	Ausgewählte Rechtsbereiche für die Bauwirtschaft | VO
Bauingenieurwesen – Baumanagement	1	Bautechnische Terminologie | VO
Bauingenieurwesen – Baumanagement	1	Von der Modellierung bis zum 3D-Druck | VO
Bauingenieurwesen – Baumanagement	2	Baukonstruktion 1 | UE
Bauingenieurwesen – Baumanagement	2	Baukonstruktion 2 | VO
Bauingenieurwesen – Baumanagement	2	Nachhaltige Baustoffkunde und Labor 2 | ILV
Bauingenieurwesen – Baumanagement	2	Betriebswirtschaftslehre für den Baubetrieb 2 | VO
Bauingenieurwesen – Baumanagement	2	Kommunikation im Berufsfeld | UE
Bauingenieurwesen – Baumanagement	2	Geotechnik 2 | ILV
Bauingenieurwesen – Baumanagement	2	Ingenieurgeologie | UE
Bauingenieurwesen – Baumanagement	2	Konstruktiver Holzbau 1 | VO
Bauingenieurwesen – Baumanagement	2	Darstellende Geometrie | VO
Bauingenieurwesen – Baumanagement	2	Geodäsie und Geoinformation | ILV
Bauingenieurwesen – Baumanagement	2	Raumordnungsrecht – Bau- und Anlagerecht | VO
Bauingenieurwesen – Baumanagement	2	Stahlbau 1 | ILV
Bauingenieurwesen – Baumanagement	2	Stahlbetonbau und Massivbau 1 | ILV
Bauingenieurwesen – Baumanagement	2	Statik und Festigkeitslehre 2 | ILV
Bauingenieurwesen – Baumanagement	3	Bauverfahrenstechnik 1 unter Berücksichtigung ressourcenschonender Baustellenabwicklung | VO
Bauingenieurwesen – Baumanagement	3	Baukonstruktion 2 | UE
Bauingenieurwesen – Baumanagement	3	Baukonstruktion 3 | VO
Bauingenieurwesen – Baumanagement	3	Brandschutz | VO
Bauingenieurwesen – Baumanagement	3	Gebäudelehre | VO
Bauingenieurwesen – Baumanagement	3	Ökologisches Bauen | VO
Bauingenieurwesen – Baumanagement	3	AVA (Ausschreibung, Vergabe, Abrechnung) | VO
Bauingenieurwesen – Baumanagement	3	Betriebswirtschaftslehre für den Baubetrieb 3 | ILV
Bauingenieurwesen – Baumanagement	3	Einführung ins wissenschaftliche Arbeiten | VO
Bauingenieurwesen – Baumanagement	3	Konstruktiver Holzbau 2 | ILV
Bauingenieurwesen – Baumanagement	3	Stahlbau 2 | ILV
Bauingenieurwesen – Baumanagement	3	Stahlbetonbau und Massivbau 2 | ILV
Bauingenieurwesen – Baumanagement	3	Geotechnik 3 | VO
Bauingenieurwesen – Baumanagement	3	Lastannahmen | VO
Bauingenieurwesen – Baumanagement	3	Software gestützte MAM für Bautechnik | ILV
Bauingenieurwesen – Baumanagement	3	Statik und Festigkeitslehre 3 | ILV
Bauingenieurwesen – Baumanagement	4	Bauverfahrenstechnik 2 | VO
Bauingenieurwesen – Baumanagement	4	Baukonstruktion 4 | VO
Bauingenieurwesen – Baumanagement	4	Wissenschaftliches Schreiben | UE
Bauingenieurwesen – Baumanagement	4	Konstruktiver Entwurf 1 | UE
Bauingenieurwesen – Baumanagement	4	Berufspraktikum | PR
Bauingenieurwesen – Baumanagement	4	Seminar zum Berufspraktikum - Praktikumsbericht | SE
Bauingenieurwesen – Baumanagement	4	Betonlabor | ILV
Bauingenieurwesen – Baumanagement	4	Holzlabor | ILV
Bauingenieurwesen – Baumanagement	4	Sanierungstage Hochbau | ILV
Bauingenieurwesen – Baumanagement	4	BIM - Einführung und Datenmanagement | ILV
Bauingenieurwesen – Baumanagement	4	Stahlbau 3 | VO
Bauingenieurwesen – Baumanagement	4	Stahlbetonbau und Massivbau 3 | VO
Bauingenieurwesen – Baumanagement	5	Architektur und Raumplanung | ILV
Bauingenieurwesen – Baumanagement	5	Planen und Bauen im Bestand | VO
Bauingenieurwesen – Baumanagement	5	Konstruktiver Entwurf 2 | UE
Bauingenieurwesen – Baumanagement	5	Technische Gebäudeausstattung | ILV
Bauingenieurwesen – Baumanagement	5	Eisenbahnwesen | VO
Bauingenieurwesen – Baumanagement	5	Verkehrswegebau | ILV
Bauingenieurwesen – Baumanagement	5	Baukonstruktion 5 | VO
Bauingenieurwesen – Baumanagement	5	Präsentation des Praktikumsberichts | UE
Bauingenieurwesen – Baumanagement	5	Lean Management | VO
Bauingenieurwesen – Baumanagement	5	Projektentwicklung | ILV
Bauingenieurwesen – Baumanagement	5	Bau- und Vertragsrecht 1 | VO
Bauingenieurwesen – Baumanagement	5	Stahlbetonbau und Massivbau 4 | ILV
Bauingenieurwesen – Baumanagement	5	Übung SBB (Software unterstützte Berechnung von Tragwerken) | UE
Bauingenieurwesen – Baumanagement	5	Betriebswirtschaftslehre für den Baubetrieb 4 (inkl. Life-Cycle-Management) | VO
Bauingenieurwesen – Baumanagement	5	Operative Baustellensteuerung | VO
Bauingenieurwesen – Baumanagement	6	Abfallwirtschaft und Baustoff Recycling | VO
Bauingenieurwesen – Baumanagement	6	Bauchemie | VO
Bauingenieurwesen – Baumanagement	6	Energetisch optimiertes Bauen | VO
Bauingenieurwesen – Baumanagement	6	Brückenbau | ILV
Bauingenieurwesen – Baumanagement	6	Hohlraumbau | ILV
Bauingenieurwesen – Baumanagement	6	Siedlungswasserbau | VO
Bauingenieurwesen – Baumanagement	6	Infrastrukturprojekte | UE
Bauingenieurwesen – Baumanagement	6	Integrale Planung (= Bachelorarbeit) | ILV
Bauingenieurwesen – Baumanagement	6	Projektmanagement | ILV
Bauingenieurwesen – Baumanagement	6	Arbeitsrecht | VO
Bauingenieurwesen – Baumanagement	6	Bau- und Vertragsrecht 2 | VO
Bauingenieurwesen – Baumanagement	6	Grundzüge des baurelevanten Umweltrechts | VO
Bauingenieurwesen – Baumanagement	6	Rechtliche Aspekte internationaler Großbauvorhaben | VO
Bauingenieurwesen – Baumanagement	6	Advanced Business English | VO
Bauingenieurwesen – Baumanagement	6	Betriebswirtschaftslehre für den Baubetrieb 5 | VO
Bioengineering	1	Chemisches Laborpraktikum I | UE
Bioengineering	1	Allgemeine und anorganische Chemie | VO
Bioengineering	1	Einführung in die organische Chemie | VO
Bioengineering	1	Mathematik | ILV
Bioengineering	1	Übungen und Tutorium zur Mathematik | UE
Bioengineering	1	Allgemeine Mikrobiologie | VO
Bioengineering	1	Mikroskopische Übungen zur Mikrobiologie | UE
Bioengineering	1	Physik | VO
Bioengineering	1	Analytische und physikalische Chemie | VO
Bioengineering	1	Stöchiometrie und Maßanalyse | VO
Bioengineering	1	Statistik zur chemischen Analytik | ILV
Bioengineering	2	Chemisch-analytisches Laborpraktikum II | UE
Bioengineering	2	Chemisch-analytisches Laborpraktikum III | UE
Bioengineering	2	Organische Chemie | VO
Bioengineering	2	Technisches Zeichnen, Maschinenkunde | ILV
Bioengineering	2	Werkstoffkunde und Fertigungstechnik | VO
Bioengineering	2	Mikrobiologie Methoden | ILV
Bioengineering	2	Spezielle Mikrobiologie | VO
Bioengineering	2	Elektrotechnik | VO
Bioengineering	2	Hydraulik und Strömungslehre | VO
Bioengineering	2	Technische Mathematik | ILV
Bioengineering	3	Allgemeines Mikrobiologie Laborpraktikum | UE
Bioengineering	3	Biochemie | VO
Bioengineering	3	Einführung in das biochemische Praktikum | ILV
Bioengineering	3	Maschinenkunde | VO
Bioengineering	3	Zellbiologie | VO
Bioengineering	3	Mechanisch-thermische Verfahrenstechnik | VO
Bioengineering	3	Mess-, Regelungs- und Sensortechnik | ILV
Bioengineering	3	Tutorium zu Verfahrenstechnischem Rechnen | ILV
Bioengineering	3	Verfahrenstechnisches Rechnen | ILV
Bioengineering	4	Technische Mikrobiologie | VO
Bioengineering	4	Angewandte Statistik | ILV
Bioengineering	4	Programmierung und Bioinformatik | ILV
Bioengineering	4	Bioanalytik | VO
Bioengineering	4	Biochemie Praktikum | UE
Bioengineering	4	Bioverfahrenstechnisches Rechnen | ILV
Bioengineering	4	Brau- und Gärungstechnik | VO
Bioengineering	4	Grundlagen der Bioverfahrenstechnik | VO
Bioengineering	4	Molekularbiologie | VO
Bioengineering	5	Ethics in Biotechnology | ILV
Bioengineering	5	Einführung in das wissenschaftliche Arbeiten | ILV
Bioengineering	5	Molekularbiologie - Laborpraktikum | UE
Bioengineering	5	Brewing laboratory with QC Focus | UE
Bioengineering	5	Qualitätskontrolle | ILV
Bioengineering	5	Gute Herstellungspraxis und Pharmazeutisches Qualitätsmanagement | VO
Bioengineering	5	QM für Qualitätsbeauftragte | ILV
Bioengineering	5	Animal Cell Technology | VO
Bioengineering	5	Inhalte aus Bioinformatik und Bioinformatische Datenanalyse | ILV
Bioengineering	5	Programmierung | ILV
Bioengineering	5	Biotechnologischer Anlagenbau und Automatisierung | VO
Bioengineering	5	GMP Seminar | ILV
Bioengineering	6	Bachelorprüfung | AP
Bioengineering	6	Berufspraktikum | PR
Bioengineering	6	Praxisreflexion | UE
Bioengineering	6	Aseptische Abfüllungstechnologien | VO
Bioengineering	6	Downstream-Processing, Proteine | VO
Bioengineering	6	Betriebshygiene | VO
Bioengineering	6	Bioprocessing Laboratory | UE
Bioengineering	6	Digitale Transformation von Prozessen | VO
Bioengineering	6	Bachelorseminar Bioinformatik | SE
Bioengineering	6	Linuxbasierte Systeme und Datenbanken | ILV
Bioengineering	6	Programmkonzeption, Programmierung, Automatisierung, Bachelorarbeit | SE
Bioengineering	6	Anlagenauslegung und GMP-Projektarbeit, Bachelorarbeit | SE
Bioengineering	6	Bachelorseminar Bioverfahrenstechnik | SE
Bioengineering	6	Downstream Processing Laboratory | UE
Bioinformatik	1	Einführung in das Programmieren | ILV
Bioinformatik	1	Grundlagen Algorithmen | VO
Bioinformatik	1	R for Data Science | ILV
Bioinformatik	1	Shell Essentials | ILV
Bioinformatik	1	Statistik | ILV
Bioinformatik	1	Proteomics and Metabolomics | ILV
Bioinformatik	1	Sequencing Lab | ILV
Bioinformatik	1	Transcriptomics and Genomics | ILV
Bioinformatik	2	Ausgewählte Themen der Bioinformatik | SE
Bioinformatik	2	Epigenetics | ILV
Bioinformatik	2	Structural Biology and Molecular Design | ILV
Bioinformatik	2	Machine Learning and AI | ILV
Bioinformatik	2	Programming for Biomedical Data Science | ILV
Bioinformatik	2	Spezielle Statistik | ILV
Bioinformatik	2	Cloud Computing | ILV
Bioinformatik	2	Container and Collaboration | ILV
Bioinformatik	2	Workflow Design | ILV
Bioinformatik	3	Datenbanksysteme | ILV
Bioinformatik	3	Software Development | ILV
Bioinformatik	3	Businessplanung und Kostenrechnung | ILV
Bioinformatik	3	Computational Systems Biology | ILV
Bioinformatik	3	Immunologie | VO
Bioinformatik	3	IP Management & Patentwesen | ILV
Bioinformatik	3	Physiologie | VO
Bioinformatik	3	Prozessmodellierung und Simulation | ILV
Bioinformatik	3	Wahlfach Bioinformatik | ILV
Bioinformatik	3	Bioinformatics for Clinical Applications | ILV
Bioinformatik	3	Biomarker and predictive medicine | ILV
Bioinformatik	3	Software als Medizinprodukt | VO
Bioinformatik	3	Projektmanagement | ILV
Bioinformatik	3	Scientific writing and thesis proposal | ILV
Bioinformatik	4	Advanced Sequencing Lab | ILV
Bioinformatik	4	Entrepreneurship | VO
Bioinformatik	4	Life Cycle Analysis | VO
Bioinformatik	4	Wahlfach Bioinformatische Spezialisierung | ILV
Bioinformatik	4	Masterarbeit
Bioinformatik	4	Masterarbeit Seminar | SE
Bioinformatik	4	Masterprüfung | AP
Biomedizinische Analytik	1	Berufsprofil | SE
Biomedizinische Analytik	1	Kommunikation | UE
Biomedizinische Analytik	1	Medical English | UE
Biomedizinische Analytik	1	Labor Basics | ILV
Biomedizinische Analytik	1	Laborsicherheit | VO
Biomedizinische Analytik	1	Literaturrecherche | ILV
Biomedizinische Analytik	1	Mikroskopie | ILV
Biomedizinische Analytik	1	Hämatologische Labordiagnostik | ILV
Biomedizinische Analytik	1	Klinisch-chemische Labordiagnostik 1 | ILV
Biomedizinische Analytik	1	Chemie | VO
Biomedizinische Analytik	1	Mathematik 1: Stöchiometrie | UE
Biomedizinische Analytik	1	Anatomie | VO
Biomedizinische Analytik	1	Physiologie | VO
Biomedizinische Analytik	1	Histologie | VO
Biomedizinische Analytik	1	Zellbiologie | VO
Biomedizinische Analytik	2	Aktuelle Entwicklungen 1 | ILV
Biomedizinische Analytik	2	Hämatologie | VO
Biomedizinische Analytik	2	Hämatologische Morphologie | ILV
Biomedizinische Analytik	2	Histologische Labordiagnostik 1 | ILV
Biomedizinische Analytik	2	Histologische Morphologie | ILV
Biomedizinische Analytik	2	Immunologie | VO
Biomedizinische Analytik	2	Instrumentelle Analytik | VO
Biomedizinische Analytik	2	Klinisch-chemische Labordiagnostik 2 | ILV
Biomedizinische Analytik	2	Klinische Chemie | VO
Biomedizinische Analytik	2	Mathematik 2: Validierung analytischer Messdaten | UE
Biomedizinische Analytik	2	Hämostaseologie | VO
Biomedizinische Analytik	2	Hämostaseologische Labordiagnostik 1 | ILV
Biomedizinische Analytik	2	Zentrallabor | ILV
Biomedizinische Analytik	2	Biochemie und Pathobiochemie | VO
Biomedizinische Analytik	3	Mathematik 3: Statistik - Einführung | VO
Biomedizinische Analytik	3	Histologische Labordiagnostik 2 | ILV
Biomedizinische Analytik	3	Immunologische Labordiagnostik | ILV
Biomedizinische Analytik	3	Pathologie | VO
Biomedizinische Analytik	3	Mikrobiologie und klinische Mikrobiologie | VO
Biomedizinische Analytik	3	Pharmakologie und Toxikologie | VO
Biomedizinische Analytik	3	Molekularbiologie | VO
Biomedizinische Analytik	3	Molekularbiologische Labordiagnostik | ILV
Biomedizinische Analytik	3	Case Studies | SE
Biomedizinische Analytik	3	Praxisreflexionsseminar 1 | SE
Biomedizinische Analytik	3	Wahlpflichtfach 1 | ILV
Biomedizinische Analytik	4	Mathematik 4: Statistik - Praktische Anwendungen | ILV
Biomedizinische Analytik	4	Wissenschaftliches Arbeiten | ILV
Biomedizinische Analytik	4	Funktionelle Labordiagnostik | ILV
Biomedizinische Analytik	4	Kardiopulmonale Funktionsdiagnostik | VO
Biomedizinische Analytik	4	Neurologische Funktionsdiagnostik | VO
Biomedizinische Analytik	4	Immunhämatologie | VO
Biomedizinische Analytik	4	Immunhämatologische Labordiagnostik | ILV
Biomedizinische Analytik	4	Mikrobiologische Labordiagnostik | ILV
Biomedizinische Analytik	4	Aktuelle Entwicklungen 2 | ILV
Biomedizinische Analytik	4	Praxisreflexionsseminar 2 | SE
Biomedizinische Analytik	4	Wahlpflichtfach 2 | ILV
Biomedizinische Analytik	4	Zytologie | VO
Biomedizinische Analytik	4	Zytologische Labordiagnostik | ILV
Biomedizinische Analytik	4	Zytologische Morphologie | ILV
Biomedizinische Analytik	5	Gendermedizin | VO
Biomedizinische Analytik	5	Medizinethik | VO
Biomedizinische Analytik	5	Professional English | UE
Biomedizinische Analytik	5	Public Health | VO
Biomedizinische Analytik	5	Proseminar Bachelorarbeit | SE
Biomedizinische Analytik	5	Praxisreflexionsseminar 3 | SE
Biomedizinische Analytik	6	Aktuelle Entwicklungen 3 | ILV
Biomedizinische Analytik	6	Gesundheitsökonomie | VO
Biomedizinische Analytik	6	Labormanagement | VO
Biomedizinische Analytik	6	Qualitätsmanagement | VO
Biomedizinische Analytik	6	Rechtsgrundlagen | VO
Biomedizinische Analytik	6	Bachelorprüfung | AP
Biomedizinische Analytik	6	Mathematik 5: Statistische Beratung | SE
Biomedizinische Analytik	6	Seminar Bachelorarbeit | SE
Bioprocess Engineering	1	Bioverfahren und Produkte | VO
Bioprocess Engineering	1	Mastercellbank Characterisation | UE
Bioprocess Engineering	1	Mikrobielle Produktionsstämme und Stammverbesserung | VO
Bioprocess Engineering	1	Messung, Regelung und Automatisierung | VO
Bioprocess Engineering	1	Thermodynamik I | VO
Bioprocess Engineering	1	Aseptic Operations | VO
Bioprocess Engineering	1	Bioprozesse: Scale Up, Transfer und Digital Twins | VO
Bioprocess Engineering	1	Differentialgleichungen | ILV
Bioprocess Engineering	1	Spezielle Statistik | ILV
Bioprocess Engineering	1	Statistische Versuchsplanung I | ILV
Bioprocess Engineering	2	Scale Up of a Brewing Process | UE
Bioprocess Engineering	2	Fermentationspraktikum | UE
Bioprocess Engineering	2	Anlagendesign und –bau | VO
Bioprocess Engineering	2	Thermodynamik II | VO
Bioprocess Engineering	2	Quality in Operation | ILV
Bioprocess Engineering	2	Technisches Risikomanagement | ILV
Bioprocess Engineering	2	Validierung | ILV
Bioprocess Engineering	2	Plattformchemikalien und Biopolymere | ILV
Bioprocess Engineering	2	Datenverarbeitung und Visualisierung | VO
Bioprocess Engineering	2	Statistische Versuchsplanung II | ILV
Bioprocess Engineering	3	Downstream Processing | VO
Bioprocess Engineering	3	Downstream Processing Praktikum | UE
Bioprocess Engineering	3	Biorefinery Concepts | VO
Bioprocess Engineering	3	Enzymtechnologie | VO
Bioprocess Engineering	3	Computational Bioprocess Engineering | VO
Bioprocess Engineering	3	Businessplanung und Kostenrechnung | ILV
Bioprocess Engineering	3	Patentwesen | ILV
Bioprocess Engineering	3	Masterarbeit
Bioprocess Engineering	4	Entrepreneurship | ILV
Bioprocess Engineering	4	Masterarbeit
Bioprocess Engineering	4	Masterarbeit Seminar | SE
Bioprocess Engineering	4	Masterprüfung | AP
Bioprocess Engineering	4	mRNA Technology, Vaccines | VO
Bioprocess Engineering	4	Life Cycle Analysis | ILV
Bioprocess Engineering	4	Medizinprodukte | VO
Bioprocess Engineering	4	Umweltbiotechnologie | VO
Biotechnologisches Qualitätsmanagement	1	Auditwesen | VO
Biotechnologisches Qualitätsmanagement	1	ISO 9001 | ILV
Biotechnologisches Qualitätsmanagement	1	Aseptic Operations | VO
Biotechnologisches Qualitätsmanagement	1	Gute Herstellungspraxis und Pharmazeutisches Qualitätsmanagement | VO
Biotechnologisches Qualitätsmanagement	1	Gute Herstellungspraxis, Eudralex | ILV
Biotechnologisches Qualitätsmanagement	1	Qualitätskontrolle und Qualitätssicherung im Prüflaboratorium | ILV
Biotechnologisches Qualitätsmanagement	1	Bioverfahren und Produkte | VO
Biotechnologisches Qualitätsmanagement	1	Immunologie | VO
Biotechnologisches Qualitätsmanagement	1	IP Management & Patentwesen | ILV
Biotechnologisches Qualitätsmanagement	1	Physiologie | VO
Biotechnologisches Qualitätsmanagement	1	Data Mining und Visualisierung | ILV
Biotechnologisches Qualitätsmanagement	1	Spezielle Statistik | ILV
Biotechnologisches Qualitätsmanagement	1	Spezielle Statistik Übung | ILV
Biotechnologisches Qualitätsmanagement	1	Statistische Versuchsplanung I | ILV
Biotechnologisches Qualitätsmanagement	2	Einführung in das wissenschaftliche Schreiben mit KI | ILV
Biotechnologisches Qualitätsmanagement	2	Six Sigma, Lean, Kaizen | ILV
Biotechnologisches Qualitätsmanagement	2	Quality in Operation | ILV
Biotechnologisches Qualitätsmanagement	2	Technisches Risikomanagement | ILV
Biotechnologisches Qualitätsmanagement	2	Validierung | ILV
Biotechnologisches Qualitätsmanagement	2	Selbstinspektion Übung | ILV
Biotechnologisches Qualitätsmanagement	2	Parenteralia | VO
Biotechnologisches Qualitätsmanagement	2	Biopharmakologie | VO
Biotechnologisches Qualitätsmanagement	2	Gute Klinische Praxis und Pharmakovigilanz | VO
Biotechnologisches Qualitätsmanagement	2	Regulatorische Anforderungen in der Arzneimittelzulassung | ILV
Biotechnologisches Qualitätsmanagement	2	Statistische Prozesskontrolle | ILV
Biotechnologisches Qualitätsmanagement	3	Businessplanung und Kostenrechnung | ILV
Biotechnologisches Qualitätsmanagement	3	Environment, Health and Safety | ILV
Biotechnologisches Qualitätsmanagement	3	Leadership | VO
Biotechnologisches Qualitätsmanagement	3	Social Skills | VO
Biotechnologisches Qualitätsmanagement	3	Operations Research Grundlagen | VO
Biotechnologisches Qualitätsmanagement	3	Projektmanagement | ILV
Biotechnologisches Qualitätsmanagement	3	Prozessmodellierung und Simulation | ILV
Biotechnologisches Qualitätsmanagement	3	Masterarbeit
Biotechnologisches Qualitätsmanagement	3	Computervalidierung | VO
Biotechnologisches Qualitätsmanagement	4	mRNA Technology, Vaccines | VO
Biotechnologisches Qualitätsmanagement	4	Life Cycle Analysis | ILV
Biotechnologisches Qualitätsmanagement	4	Medizinprodukte | ILV
Biotechnologisches Qualitätsmanagement	4	Statistische Versuchsplanung II | ILV
Biotechnologisches Qualitätsmanagement	4	Entrepreneurship | ILV
Biotechnologisches Qualitätsmanagement	4	Masterarbeit
Biotechnologisches Qualitätsmanagement	4	Masterarbeit Seminar | SE
Biotechnologisches Qualitätsmanagement	4	Masterprüfung | AP
Clinical Engineering	1	Angewandte Mathematik I | ILV
Clinical Engineering	1	Elektrotechnik Labor | UE
Clinical Engineering	1	Grundlagen der Elektrotechnik | ILV
Clinical Engineering	1	Baukonstruktionslehre | ILV
Clinical Engineering	1	Bauphysik | VO
Clinical Engineering	1	Anatomie | VO
Clinical Engineering	1	Physiologie | VO
Clinical Engineering	1	Medical English for Professionals | UE
Clinical Engineering	1	Strukturen und Abläufe in Gesundheitseinrichtungen | ILV
Clinical Engineering	1	Aufbau und Struktur von IT Netzen | ILV
Clinical Engineering	2	Angewandte Mathematik II | ILV
Clinical Engineering	2	Physikalische Grundlagen HKLS | ILV
Clinical Engineering	2	Elektro-Installationen | ILV
Clinical Engineering	2	Elektro-Installationstechnik-Labor | UE
Clinical Engineering	2	Energieversorgung und Netzsicherheit | VO
Clinical Engineering	2	Gebäudeleittechnik | ILV
Clinical Engineering	2	Grundlagen Managementmethoden | VO
Clinical Engineering	2	Qualitätsmanagement | ILV
Clinical Engineering	2	Errichtung von Kommunikations- und IT Netzen | ILV
Clinical Engineering	2	Security in IT Netzen | ILV
Clinical Engineering	3	Grundlagen der Medizintechnik | ILV
Clinical Engineering	3	Betrieb und Wartung | ILV
Clinical Engineering	3	Krankenhaushygiene | VO
Clinical Engineering	3	Dimensionierung HKLS | ILV
Clinical Engineering	3	Installation und Klimatisierung | ILV
Clinical Engineering	3	Technische Versorgung (Gase) | ILV
Clinical Engineering	3	IT-Systeme und Interoperabilität im Gesundheitswesen | ILV
Clinical Engineering	3	Prozessmanagement | ILV
Clinical Engineering	3	Requirements Engineering und Projektmanagement | ILV
Clinical Engineering	4	Medizintechnik Anwendungen | ILV
Clinical Engineering	4	Medizintechnik Funktionseinheiten | ILV
Clinical Engineering	4	Krankenhausplanung | ILV
Clinical Engineering	4	Organisations- und Betriebsführung | ILV
Clinical Engineering	4	Grundlagen des Building Information Modeling | ILV
Clinical Engineering	4	Klinische Untersuchungs- und Behandlungsmethoden | ILV
Clinical Engineering	5	Methoden wissenschaftlichen Arbeitens | SE
Clinical Engineering	5	Technisches Projekt | SE
Clinical Engineering	5	Einführung Betriebswirtschaftslehre | VO
Clinical Engineering	5	Kosten- und Investitionsrechnung | ILV
Clinical Engineering	5	Personalmanagement | ILV
Clinical Engineering	5	Vergabe- und Vertragsrecht im Gesundheitswesen | ILV
Clinical Engineering	5	Risikomanagement im Krankenhaus | ILV
Clinical Engineering	5	Technische Sicherheit | ILV
Clinical Engineering	6	Bachelorkolloquium | AP
Clinical Engineering	6	Seminar Bachelorarbeit | SE
Clinical Engineering	6	Exkursionen | SE
Clinical Engineering	6	Praxisdialog | SE
Clinical Engineering	6	Berufspraktikum (min. 6 Wochen á 38,5 Wochenstunden) | PR
Clinical Engineering	6	Praxisbegleitendes Seminar | SE
Clinical Engineering	6	Arbeits- und Sozialrecht | ILV
            """.trimIndent()
        )
        append('\n')
        append(
            """
Clinical Engineering	6	Medizin- und PatientInnenrecht | ILV
Computer Science and Digital Communications	1	Betriebssysteme | ILV
Computer Science and Digital Communications	1	Konzepte der IT | ILV
Computer Science and Digital Communications	1	Programmierung 1 | ILV
Computer Science and Digital Communications	1	Teamarbeit | ILV
Computer Science and Digital Communications	1	Mathematik 1 | VO
Computer Science and Digital Communications	1	Mathematik 1 | UE
Computer Science and Digital Communications	1	Digital Communications | ILV
Computer Science and Digital Communications	2	Datenbanken | ILV
Computer Science and Digital Communications	2	Programmierung 2 | ILV
Computer Science and Digital Communications	2	Mathematik 2 | VO
Computer Science and Digital Communications	2	Mathematik 2 | UE
Computer Science and Digital Communications	2	Network Applications | ILV
Computer Science and Digital Communications	2	Professional Presentations | ILV
Computer Science and Digital Communications	2	Web Technologies | ILV
Computer Science and Digital Communications	3	DevOps | ILV
Computer Science and Digital Communications	3	Internet of Things | ILV
Computer Science and Digital Communications	3	IT Security Fundamentals | ILV
Computer Science and Digital Communications	3	Introduction to AI and Data Science | ILV
Computer Science and Digital Communications	3	Research Methods | SE
Computer Science and Digital Communications	3	Software Engineering | ILV
Computer Science and Digital Communications	4	Projektmanagement | ILV
Computer Science and Digital Communications	4	Wahlfach-Projekt 1 | UE
Computer Science and Digital Communications	4	Algorithmen & Datenstrukturen | ILV
Computer Science and Digital Communications	4	Human Computer Interaction | ILV
Computer Science and Digital Communications	4	IoT Applications | ILV
Computer Science and Digital Communications	4	Advanced AI and Data Science | ILV
Computer Science and Digital Communications	4	Game Development | ILV
Computer Science and Digital Communications	4	Secure Admin Tools | ILV
Computer Science and Digital Communications	4	Mobile App Development | ILV
Computer Science and Digital Communications	5	Business Administration | ILV
Computer Science and Digital Communications	5	Wahlfach-Projekt 2 | UE
Computer Science and Digital Communications	5	Green IT | ILV
Computer Science and Digital Communications	5	Verteilte Systeme | ILV
Computer Science and Digital Communications	5	Modern Networks | ILV
Computer Science and Digital Communications	5	Deep Learning | ILV
Computer Science and Digital Communications	5	Virtual and Augmented Reality | ILV
Computer Science and Digital Communications	5	Ausgewählte Kapitel der IT-Security | ILV
Computer Science and Digital Communications	5	Advanced Web Engineering | ILV
Computer Science and Digital Communications	6	Bachelorarbeit | SE
Computer Science and Digital Communications	6	Bachelorprüfung | AP
Computer Science and Digital Communications	6	Berufspraktikum | PR
Computer Science and Digital Communications	6	Selected Topics | ILV
Computer Science and Digital Communications	6	IT Prozess- und Qualitätsmanagement | ILV
Computer Science and Digital Communications	6	IT-Recht | VO
Diätologie	1	Berufskunde | ILV
Diätologie	1	Einführung Gesundheitswesen und -ökonomie | VO
Diätologie	1	Projektmanagement | ILV
Diätologie	1	Energie- und Nährstoffberechnung/EDV | ILV
Diätologie	1	Ernährungsmanagement in der Prävention 1 | ILV
Diätologie	1	Verpflegungspraxis 1 | UE
Diätologie	1	Biochemie | VO
Diätologie	1	Chemie | VO
Diätologie	1	Ernährungslehre 1 | VO
Diätologie	1	Ernährungslehre 2, Anthropometrie | ILV
Diätologie	1	Hygiene und Mikrobiologie | VO
Diätologie	1	Lebensmittelkunde/-technologie | ILV
Diätologie	1	Lebensmittelrecht | VO
Diätologie	1	Literaturrecherche, Zitieren und wissenschaftliches Schreiben | ILV
Diätologie	1	Wiss. Evidenz aus Literaturübersichtsarbeiten | SE
Diätologie	1	Allgemeine Anatomie | VO
Diätologie	1	Allgemeine Physiologie | VO
Diätologie	2	English for dietitians | UE
Diätologie	2	Ernährungsbildung/-pädagogik | UE
Diätologie	2	Grundlagen Ernährungsbildung/-pädagogik | ILV
Diätologie	2	Lebensmittelhygiene | VO
Diätologie	2	Psychologie/Ernährungspsychologie | ILV
Diätologie	2	Soziologie/Ernährungssoziologie | ILV
Diätologie	2	Allgemeine Pathologie | VO
Diätologie	2	Ernährungsphysiologie | VO
Diätologie	2	Fachspezifische Anatomie | VO
Diätologie	2	Ernährungsmanagement in der Prävention 2, alternative Ernährungsformen, Trenddiäten | ILV
Diätologie	2	International/Professional cooking | UE
Diätologie	2	Reflexion Praxislernphase | SE
Diätologie	2	Verpflegungspraxis 2 | UE
Diätologie	2	Einführung Diaetologischer Prozess und Clinical Reasoning | ILV
Diätologie	2	Empirisches Arbeiten und Biostatistik | ILV
Diätologie	2	Qualitative Forschungsmethoden | ILV
Diätologie	2	Ernährung - Schwangere, Stillende, Säuglinge, Kleinkinder | ILV
Diätologie	2	Ernährung im Alter, angepasste Vollkost | ILV
Diätologie	2	Global Nutrition / Nachhaltigkeit SDG | ILV
Diätologie	2	Interkulturelle Ernährung | ILV
Diätologie	2	Präventionsprojekt | UE
Diätologie	2	Sporternährung | ILV
Diätologie	3	Angewandte Klinische Diätetik 1 | UE
Diätologie	3	Reflexion Diätologischer Praxis 1 | SE
Diätologie	3	Grundlagen der Labordiagnostik und Befundung | ILV
Diätologie	3	Klinische Physiologie | VO
Diätologie	3	Angewandtes qualitatives Forschungsprojekt | SE
Diätologie	3	Angewandtes quantitatives Forschungsprojekt | SE
Diätologie	3	Diaetologischer Prozess und Fachsprache Diätologie | ILV
Diätologie	3	Beratungstechnik 1 | UE
Diätologie	3	Beratungstechnik 2 | UE
Diätologie	3	Grundlagen Beratungstechnik | ILV
Diätologie	3	Klinische Psychologie | ILV
Diätologie	3	Verhaltensmodifikation | ILV
Diätologie	3	Einführung enterale/parenterale Ernährung | ILV
Diätologie	3	KD/DP-Endokrinologie, Stoffwechsel, Rheumatischer Formenkreis | ILV
Diätologie	3	KD/DP-Gastroenterologie und Hepatologie | ILV
Diätologie	4	Angewandte Klinische Diätetik 2 | UE
Diätologie	4	Klinische Grundlagen Pädiatrie | VO
Diätologie	4	Pharmakologie und Toxikologie | VO
Diätologie	4	Planung und Design von diätologischen Studien und Literaturübersichtsarbeiten 1 | ILV
Diätologie	4	Recherchieren, interpretieren und anwenden ernährungsepidemiologischer Evidenz | ILV
Diätologie	4	Beratungstechnik 3 | UE
Diätologie	4	Einführung in das palliativmed. Setting | UE
Diätologie	4	KD/DP-Allergologie | ILV
Diätologie	4	KD/DP-Chirurgie, Adipositaschirurgie | ILV
Diätologie	4	KD/DP-Geriatrie und Gerontologie | ILV
Diätologie	4	KD/DP-Nephrologie, Hypertensiologie, Kardiologie | ILV
Diätologie	4	KD/DP-Neurologie und Psychiatrie | ILV
Diätologie	4	KD/DP-Onkologie, Intensivmedizin | ILV
Diätologie	4	KD/DP-Pädiatrie | ILV
Diätologie	4	Reflexion Diätologischer Praxis 2 | SE
Diätologie	4	Sonder- und Spezialdiäten | ILV
Diätologie	5	Planung und Design von diätologischen Studien und Literaturübersichtsarbeiten 2 | ILV
Diätologie	5	Interkulturelles Management in der Diätologie | ILV
Diätologie	5	Reflexion Praxislernphase Klinik | SE
Diätologie	5	Berufs- und Medizinethik | ILV
Diätologie	5	International/professional Synapse Week | SE
Diätologie	6	Bachelorworkshop | ILV
Diätologie	6	Vertiefender Diaetologischer Prozess und ICF-Diätetik | VO
Diätologie	6	Wissenschaftliche Vertiefung Bachelor | SE
Diätologie	6	Reflexion Diätologischer Praxis 3 | SE
Diätologie	6	Dietetic Counseling | UE
Diätologie	6	Wahlfach | ILV
Diätologie	6	Arbeiten im interdisziplinären Team | ILV
Diätologie	6	Betriebswirtschaft für die Diätologische Praxis | ILV
Diätologie	6	Einführung Berufs- und Gesundheitspolitik | ILV
Diätologie	6	Gesundheits- und berufsspezifisches Marketing | ILV
Diätologie	6	Qualitätsmanagement in der Diätologie | ILV
Diätologie	6	Rechtsgrundlagen der Gesundheitsberufe | ILV
Elementarpädagogik	1	Erste Hilfe - Erstversorgung von Kindern | UE
Elementarpädagogik	1	Ganzheitliche Gesundheit und Nachhaltigkeit in der Elementarpädagogik | ILV
Elementarpädagogik	1	Geschichte der Elementarpädagogik | ILV
Elementarpädagogik	1	Klassische frühpädagogische Handlungskonzepte | ILV
Elementarpädagogik	1	Ästhetische Bildung I: Bewegung | ILV
Elementarpädagogik	1	Ästhetische Bildung I: Gestalten | ILV
Elementarpädagogik	1	Ästhetische Bildung I: Musik, Stimme, Ausdruck | ILV
Elementarpädagogik	1	Didaktik | ILV
Elementarpädagogik	1	Schreibwerkstatt | SE
Elementarpädagogik	1	Lernprozessreflexion I | SE
Elementarpädagogik	1	Young Child Observation I | SE
Elementarpädagogik	1	Sozialisation und Hospitation | UE
Elementarpädagogik	2	Entwicklungstheorien | ILV
Elementarpädagogik	2	Neue frühpädagogische Handlungskonzepte | ILV
Elementarpädagogik	2	Psychodynamische Entwicklungstheorien | ILV
Elementarpädagogik	2	Sozioökonomische Theorien und rechtlichen Grundlagen der Elementarpädagogik | VO
Elementarpädagogik	2	Ästhetische Bildung II: Bewegung | ILV
Elementarpädagogik	2	Ästhetische Bildung II: Gestalten | ILV
Elementarpädagogik	2	Ästhetische Bildung II: Musik, Stimme, Ausdruck | ILV
Elementarpädagogik	2	Bildungsprozesse beobachten und planen | ILV
Elementarpädagogik	2	Grundlagen wissenschaftlichen Arbeitens | ILV
Elementarpädagogik	2	Lektüreseminar | SE
Elementarpädagogik	2	Lernprozessreflexion II | SE
Elementarpädagogik	2	Young Child Observation II | SE
Elementarpädagogik	2	Hinführung zu Methoden der Elementarpädagogik | UE
Elementarpädagogik	3	Beratung und Gesprächsführung mit Eltern | ILV
Elementarpädagogik	3	Elementarpädagogik International | ILV
Elementarpädagogik	3	Familien- und Unterstützungssysteme | ILV
Elementarpädagogik	3	Transitionsprozesse in der frühen Kindheit | ILV
Elementarpädagogik	3	Ästhetische Bildung III: Bewegung | ILV
Elementarpädagogik	3	Ästhetische Bildung III: Gestalten | ILV
Elementarpädagogik	3	Ästhetische Bildung III: Musik, Stimme, Ausdruck | ILV
Elementarpädagogik	3	Elementarpädagogische Handlungsfelder: Medien, Sprache und Sexualität | ILV
Elementarpädagogik	3	Grundlagen der empirischen Sozialforschung | VO
Elementarpädagogik	3	Lehrforschungsprojekt I | SE
Elementarpädagogik	3	Lernprozessreflexion III | SE
Elementarpädagogik	3	Work Discussion I | SE
Elementarpädagogik	3	Anwendung von Methoden der Elementarpädagogik | UE
Elementarpädagogik	4	Diversität und Lebenswelten | ILV
Elementarpädagogik	4	Ethische Grundlagen von Erziehung und Bildung | ILV
Elementarpädagogik	4	Ästhetische Bildung IV: Bewegung | ILV
Elementarpädagogik	4	Ästhetische Bildung IV: Gestalten | ILV
Elementarpädagogik	4	Ästhetische Bildung IV: Musik, Stimme, Ausdruck | ILV
Elementarpädagogik	4	Psychodynamik und Spiel | ILV
Elementarpädagogik	4	Elementarpädagogische Handlungsfelder: Inklusion, Sprache, MINT | ILV
Elementarpädagogik	4	Lehrforschungsprojekt II | SE
Elementarpädagogik	4	Qualitative Methoden | ILV
Elementarpädagogik	4	Quantitative Methoden | ILV
Elementarpädagogik	4	Lernprozessreflexion IV | SE
Elementarpädagogik	4	Work Discussion II | SE
Elementarpädagogik	4	Gestaltung elementarpädagogischer Praxis | UE
Elementarpädagogik	5	Beratung und Gesprächsführung im Team | ILV
Elementarpädagogik	5	Organisation und Management I | ILV
Elementarpädagogik	5	Ästhetische Bildung V: Bewegung | ILV
Elementarpädagogik	5	Ästhetische Bildung V: Gestalten | ILV
Elementarpädagogik	5	Ästhetische Bildung V: Musik, Stimme, Ausdruck | ILV
Elementarpädagogik	5	Diagnostik, Traumapädagogik, Prävention | ILV
Elementarpädagogik	5	Krise, Krisenintervention und Trauma | ILV
Elementarpädagogik	5	Konzeptseminar Bachelorarbeit | SE
Elementarpädagogik	5	Lernprozessreflexion V | SE
Elementarpädagogik	5	Work Discussion III | SE
Elementarpädagogik	5	Analyse elementarpädagogischer Praxis | UE
Elementarpädagogik	6	Organisation und Management II | ILV
Elementarpädagogik	6	Rechtliche Grundlagen: Vertiefung | VO
Elementarpädagogik	6	Diagnostisches Fallverstehen | ILV
Elementarpädagogik	6	Wahlfach: Interprofessionelle Zusammenarbeit mit ausgewählten Gesundheitsberufen | ILV
Elementarpädagogik	6	Wahlfach: Multi- und translinguale Lebenswelten von Kindern | ILV
Elementarpädagogik	6	Bachelorprüfung | AP
Elementarpädagogik	6	Seminar zur Bachelorarbeit | SE
Elementarpädagogik	6	Lernprozessreflexion VI | SE
Elementarpädagogik	6	Organisationsbeobachtung | SE
Elementarpädagogik	6	Evaluation und Konzeption | UE
Ergotherapie	1	Aktivitäts- und Performanzanalyse | ILV
Ergotherapie	1	Funktionelle Anwendung anatomischer Grundlagen | ILV
Ergotherapie	1	Grundlagen der Anatomie und Physiologie | ILV
Ergotherapie	1	Alltagsbewältigung in Lebensphasen | UE
Ergotherapie	1	Elemente der Handlungsperformanz: Person, Handlung, Umwelt | ILV
Ergotherapie	1	Geschichte und Modelle der Ergotherapie | ILV
Ergotherapie	1	Grundlagen der evidenzbasierten Praxis | ILV
Ergotherapie	1	Journal Club | SE
Ergotherapie	1	Grundlagen der Selbstreflexion, Kommunikation und Gesprächsführung | ILV
Ergotherapie	1	Professionelle Haltung entwickeln – Veränderungsprozesse verstehen | ILV
Ergotherapie	2	Befundungs- und Evaluierungsverfahren | ILV
Ergotherapie	2	Betätigungsanliegen erheben | ILV
Ergotherapie	2	Alltagsorientierte Interventionen | ILV
Ergotherapie	2	Angewandtes professionelles Reasoning: Intervention | SE
Ergotherapie	2	Interventionen zur Förderung von Fähigkeitskomponenten und Fertigkeiten | ILV
Ergotherapie	2	Kreative Medien in der Ergotherapie | ILV
Ergotherapie	2	Ergotherapie im Arbeitsfeld Psychiatrie | ILV
Ergotherapie	2	Grundlagen der Ergotherapie im Arbeitsfeld Psychiatrie | ILV
Ergotherapie	2	Fachsupervision: Praxislernphase 2. Semester | SE
Ergotherapie	2	Gesprächsführung und Anleitungskompetenz | ILV
Ergotherapie	2	Theorie-Praxis-Transfer: Vorbereitung Praxislernphase 2. Semester | ILV
Ergotherapie	3	Ergotherapie mit Menschen mit nicht übertragbaren chronischen Erkrankungen | ILV
Ergotherapie	3	Nicht übertragbare chronische Erkrankungen: Grundlagen | VO
Ergotherapie	3	Ergotherapie mit alten Menschen | ILV
Ergotherapie	3	Grundlagen zum Lebensabschnitt Alter | ILV
Ergotherapie	3	Ergotherapie mit Kindern und Jugendlichen | ILV
Ergotherapie	3	Medizinische Grundlagen für Ergotherapie mit Kindern und Jugendlichen | VO
Ergotherapie	3	Ergotherapie in den Arbeitsfeldern Neurologie, Traumatologie, Orthopädie und Rheumatologie | ILV
Ergotherapie	3	Fallarbeit im Arbeitsfeld Neurologie | ILV
Ergotherapie	3	Fallarbeit im Arbeitsfeld Traumatologie, Orthopädie und Rheumatologie | ILV
Ergotherapie	3	Professionelle Haltung und professionelles Handeln erweitern | ILV
Ergotherapie	3	Theorie-Praxis-Transfer: Selbstkonfrontation und Selbstfürsorge | ILV
Ergotherapie	3	Literaturreview | SE
Ergotherapie	3	Literaturreview: Einführung | ILV
Ergotherapie	4	Arbeiten mit ergotherapeutischen Gruppen und Teamarbeit: reflexives Begleitseminar | SE
Ergotherapie	4	Planen und leiten von ergotherapeutischen Gruppen: Grundlagen und Praxis | ILV
Ergotherapie	4	Community Innovation LAB: Schwerpunkt gesundes Altern | ILV
Ergotherapie	4	Gesunde Arbeitswelten | ILV
Ergotherapie	4	Fachsupervision: Praxislernphase 4. Semester | SE
Ergotherapie	4	Professionell Handeln - individuelles Wahlfach | SE
Ergotherapie	4	Professionelles Handeln vertiefen: Veränderungsprozesse leiten und begleiten | ILV
Ergotherapie	4	Theorie-Praxis-Transfer: Vorbereitung Praxislernphase 4. Semester | ILV
Ergotherapie	5	Berufspolitik und Marketing | ILV
Ergotherapie	5	Bewältigung herausfordernder Situationen im interprofessionellen und internationalen Kontext | SE
Ergotherapie	5	Freiberuflichkeit - Betriebswirtschaftliche Grundlagen | ILV
Ergotherapie	5	Gesundheitswesen und Recht | VO
Ergotherapie	5	Fachsupervision: Praxislernphase 5. Semester | SE
Ergotherapie	5	Theorie-Praxis-Transfer: Vorbereitung Praxislernphase 5. Semester | ILV
Ergotherapie	5	Transitionsprozesse gestalten - Innovation | ILV
Ergotherapie	5	Bachelorprüfung | AP
Ergotherapie	5	Bachelorseminar 1: Erstellen wissenschaftlicher Konzepte | SE
Ergotherapie	5	Wissenschaftlicher Forschungsprozess | ILV
Ergotherapie	5	freies Wahlfach 1 | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie im Arbeitsfeld Kinder- und Jugendpsychiatrie - Wahlfach | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie im Arbeitsfeld Neurologie - Wahlfach | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie im Arbeitsfeld Orthopädie, Rheumatologie und Traumatologie - Wahlfach | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie im Arbeitsfeld Psychiatrie - Wahlfach | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie mit alten Menschen - Wahlfach | ILV
Ergotherapie	5	Vertiefung in die Ergotherapie mit Kindern und Jugendlichen - Wahlfach | ILV
Ergotherapie	6	freies Wahlfach 2 | ILV
Ergotherapie	6	Occupational Science and Global Citizenship | ILV
Ergotherapie	6	Weiterentwicklung der Ergotherapie in unterschiedlichen Settings und Rollen | ILV
Ergotherapie	6	Fachsupervision – Praxislernphase 6. Semester | SE
Ergotherapie	6	Theorie-Praxis-Transfer: Vorbereitung Praxislernphase 6. Semester | ILV
Ergotherapie	6	Transitionsprozesse gestalten - Entrepreneurship | ILV
Ergotherapie	6	Bachelorseminar 2: Angewandte wissenschaftliche Methodik | SE
Ergotherapie	6	Occupational Therapy and Social Transformation - Integration | ILV
Ergotherapie	6	Occupational Therapy and Social Transformation - Introduction | ILV
Ergotherapie	6	Emerging Roles and Future of Occupational Therapy | ILV
Ergotherapie	6	Applied Research Methology | SE
Ergotherapie	6	Fieldwork Placement | PR
Ergotherapie	6	Advancing Occupational Justice | ILV
Funktionsdiagnostik	1	Studium starten | SE
Funktionsdiagnostik	1	Anatomie | VO
Funktionsdiagnostik	1	Pathologie | VO
Funktionsdiagnostik	1	Physiologie | VO
Funktionsdiagnostik	1	Wissenschaftliches Arbeiten und Literaturrecherche | SE
Funktionsdiagnostik	1	Evidenzbasierte Praxis und Critically Appraised Topic | SE
Funktionsdiagnostik	1	Anamneseerhebung, Kommunikation und Gesprächsführung | ILV
Funktionsdiagnostik	1	Kardio-, Pulmo-, Neurologische Grundlagen | ILV
Funktionsdiagnostik	1	Ergonomie | ILV
Funktionsdiagnostik	1	Patient*innensicherheit und -management | VO
Funktionsdiagnostik	1	PSG: Durchführung, Interpretation & Therapie | ILV
Funktionsdiagnostik	1	Schlaf - Grundlagen | VO
Funktionsdiagnostik	2	Berufsbild und Gesetzlicher Rahmen | VO
Funktionsdiagnostik	2	Biomedizinischer Befund in der Funktionsdiagnostik | VO
Funktionsdiagnostik	2	Ethik in der Funktionsdiagnostik | VO
Funktionsdiagnostik	2	Forschung in der Funktionsdiagnostik | VO
Funktionsdiagnostik	2	Notfallmanagement und Erste Hilfe | ILV
Funktionsdiagnostik	2	Pharmakologie | VO
Funktionsdiagnostik	2	Fallstudien | SE
Funktionsdiagnostik	2	Weiterführende kardiologische Untersuchungen | ILV
Funktionsdiagnostik	2	Weiterführende neurologische Untersuchungen | ILV
Funktionsdiagnostik	2	Weiterführende pulmologische Untersuchungen | ILV
Funktionsdiagnostik	2	Scoring, weiterführende Untersuchungen | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Alterstheorien und -modelle | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Settingspezifische Strukturen und integrierte Versorgungsmodelle | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Lebensqualität und Entstigmatisierung | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Ausgewählte gerontologische Phänomene | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Psychosoziale Begleitung | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Gerontologische Assessments | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Multimorbidität und geriatrische Syndrome | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Gerontopsychiatrische Erkrankungen | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Prävention im familien- und gemeindenahen Setting | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Prävention im Akut- und Langzeitpflegesetting | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Wissenschaftliches Arbeiten | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Quantitative Forschung und Statistik | ILV
Gerontologische Gesundheits- und Krankenpflege	1	Qualitative Forschung | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Kommunikation und Beratung | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Digital Health | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Komplementäre Pflegemethoden | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Palliative Care und Advanced Care Planning | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Gesundheitsförderung im Akut- und Langzeitpflegesetting | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Gesundheitsförderung im familien- und gemeindenahen Setting | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Suizid- und Gewaltprävention | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Gesundheitsförderung und Prävention für Menschen mit chronischen Erkrankungen und Vulnerabilitäten | ILV
Gerontologische Gesundheits- und Krankenpflege	2	Reflektierte Praxis | PR
Gerontologische Gesundheits- und Krankenpflege	2	Abschlussarbeit | SE
Gerontologische Gesundheits- und Krankenpflege	2	Kommissionelle Abschlussprüfung | AP
Gesundheits- und Krankenpflege	1	Grundlagen der Gesundheits- und Krankenpflege und Pflegeprozess | ILV
Gesundheits- und Krankenpflege	1	Gesundheits- und Krankenpflege im Kontext mit Diagnostik und Therapie 1 | UE
Gesundheits- und Krankenpflege	1	Schwangerschaft, Geburt, Wochenbett und Neugeborenenpflege | ILV
Gesundheits- und Krankenpflege	1	Biologie, Anatomie, Physiologie | VO
Gesundheits- und Krankenpflege	1	Allgemeine Pathologie | VO
Gesundheits- und Krankenpflege	1	Angewandte Hygiene, Infektionslehre | ILV
Gesundheits- und Krankenpflege	1	Psychologie, Pädagogik, Soziologie | ILV
Gesundheits- und Krankenpflege	1	Fertigkeitentraining "Bewegungs- und Wahrnehmungskonzepte I" | UE
Gesundheits- und Krankenpflege	1	Fertigkeitentraining "Notfallmanagement, Basic Life Support" | UE
Gesundheits- und Krankenpflege	1	Praxislernphase 1 - Einführung und Reflexion, einschl. Resilienztraining | ILV
Gesundheits- und Krankenpflege	2	Gesundheits- und Krankenpflege im Kontext mit Diagnostik und Therapie 2 | UE
Gesundheits- und Krankenpflege	2	Gesundheits- und Krankenpflege in speziellen Settings 1 | ILV
Gesundheits- und Krankenpflege	2	Ethik und diversitätskompetente Gesundheits- und Krankenpflege | ILV
Gesundheits- und Krankenpflege	2	Palliative Care | ILV
Gesundheits- und Krankenpflege	2	Gerontologische Gesundheits- und Krankenpflege und Gerontologie | ILV
Gesundheits- und Krankenpflege	2	Ernährungslehre | ILV
Gesundheits- und Krankenpflege	2	Geriatrie, Gerontopsychiatrie | VO
Gesundheits- und Krankenpflege	2	Allgemeine und berufsspezifische Rechtsgrundlagen | ILV
Gesundheits- und Krankenpflege	2	Ausgewählte Erkrankungen einschließlich Diagnostik und Therapie 1 | VO
Gesundheits- und Krankenpflege	2	Einführung in die Pflegewissenschaft und Pflegeforschung | ILV
Gesundheits- und Krankenpflege	2	Fertigkeitentraining "Bewegungs- und Wahrnehmungskonzepte II" | UE
Gesundheits- und Krankenpflege	2	Praxislernphase 2 - Einführung und Reflexion | ILV
Gesundheits- und Krankenpflege	3	Kinder- und Jugendlichen Gesundheits- und Krankenpflege | ILV
Gesundheits- und Krankenpflege	3	Gesundheits- und Krankenpflege in speziellen Settings 2 | ILV
Gesundheits- und Krankenpflege	3	Psychiatrische Gesundheits- und Krankenpflege | ILV
Gesundheits- und Krankenpflege	3	Familien- und gemeindenahe Gesundheits- und Krankenpflege | ILV
Gesundheits- und Krankenpflege	3	Ausgewählte Erkrankungen einschließlich Diagnostik und Therapie 2 | VO
Gesundheits- und Krankenpflege	3	Pharmakologie, Toxikologie | VO
Gesundheits- und Krankenpflege	3	Theorien, Modelle und Konzepte in der Gesundheits und Krankenpflege | ILV
Gesundheits- und Krankenpflege	3	Fertigkeitentraining "Wundmanagement" | UE
Gesundheits- und Krankenpflege	3	Praxislernphase 3 - Einführung und Reflexion | ILV
Gesundheits- und Krankenpflege	4	Gesundheits- und Krankenpflege im Kontext mit Diagnostik und Therapie 3 | UE
Gesundheits- und Krankenpflege	4	Gesundheits- und Krankenpflege in speziellen Settings 3 | ILV
Gesundheits- und Krankenpflege	4	Fallbasierter Pflegeprozess, Pflegeklassifikationssysteme, Clinical Assessment | ILV
Gesundheits- und Krankenpflege	4	Leben mit chronischer Krankheit | ILV
Gesundheits- und Krankenpflege	4	Kommunikation und Konfliktlösung | ILV
Gesundheits- und Krankenpflege	4	Ausgewählte Erkrankungen einschließlich Diagnostik und Therapie 3 | VO
Gesundheits- und Krankenpflege	4	Qualitative und quantitative Forschungsmethoden, Statistik | ILV
Gesundheits- und Krankenpflege	4	Forschungsanwendung (EBN) | SE
Gesundheits- und Krankenpflege	4	Fertigkeitentraining "Deeskalation" | UE
Gesundheits- und Krankenpflege	4	Praxislernphase 4 - Einführung und Reflexion | ILV
Gesundheits- und Krankenpflege	5	Gesundheitsförderung, Prävention, Pflegeepidemiologie | ILV
Gesundheits- und Krankenpflege	5	Grundlagen und Methoden der Beratung | ILV
Gesundheits- und Krankenpflege	5	Fertigkeiten- / Simulationstraining "Advanced Life Support, Monitoring" | UE
Gesundheits- und Krankenpflege	5	Praxislernphase 5 - Einführung und Reflexion | ILV
Gesundheits- und Krankenpflege	6	Angewandte Pflegewissenschaft (Bachelorarbeit) | SE
Gesundheits- und Krankenpflege	6	Vorbereitungsworkshop kommissionelle Abschlussprüfung | ILV
Gesundheits- und Krankenpflege	6	Kommissionelle Abschlussprüfung | AP
Gesundheits- und Krankenpflege	6	Kinder- und Jugendlichen Gesundheits- und Krankenpflege - Vertiefung | ILV
Gesundheits- und Krankenpflege	6	Psychiatrische Gesundheits- und Krankenpflege - Vertiefung | ILV
Gesundheits- und Krankenpflege	6	Familien- und gemeindenahe Gesundheits- und Krankenpflege - Vertiefung | ILV
Gesundheits- und Krankenpflege	6	Leben mit chronischer Krankheit - Vertiefung | ILV
Gesundheits- und Krankenpflege	6	Gerontologische, Psychogeriatrische Gesundheits- und Krankenpflege - Vertiefung | ILV
Gesundheits- und Krankenpflege	6	Angewandte Pflegeforschung | ILV
Gesundheits- und Krankenpflege	6	Gesundheitswesen, Führung und Organisation | ILV
Gesundheits- und Krankenpflege	6	Geschichte und Berufsfeldentwicklung der Gesundheits- und Krankenpflege | ILV
Gesundheits- und Krankenpflege	6	Digital Health, Qualitätsmanagement | ILV
Gesundheits- und Krankenpflege	6	Case- und Caremanagement | ILV
Gesundheits- und Krankenpflege	6	Fertigkeitentraining "Komplementäre Pflegemethoden" | UE
Gesundheits- und Krankenpflege	6	Praxislernphase 6 - Einführung und Reflexion | ILV
Health Assisting Engineering	1	Assistierende Technologien | VO
Health Assisting Engineering	1	Barrierefreiheit und Diversity | ILV
Health Assisting Engineering	1	Einführung in Health Assisting Engineering | VO
Health Assisting Engineering	1	Angewandte Mathematik | ILV
Health Assisting Engineering	1	Angewandtes Interdisziplinäres Wissensmanagement | ILV
Health Assisting Engineering	1	Epidemiologie | VO
Health Assisting Engineering	1	Modelle, Konzepte und Klassifikationen im Gesundheitswesen | ILV
Health Assisting Engineering	1	Allgemeine Pathologie und Hygiene | VO
Health Assisting Engineering	1	Anatomie und Physiologie | ILV
Health Assisting Engineering	1	Ausgewählte Krankheitsbilder | ILV
Health Assisting Engineering	1	Analyse der Handlungsfähigkeit von Menschen | ILV
Health Assisting Engineering	1	Anatomie in vivo | ILV
Health Assisting Engineering	1	Bewegungsanalyse und Biomechanik | ILV
Health Assisting Engineering	1	Einführung in die Informatik | ILV
Health Assisting Engineering	1	Kommunikationssysteme und Datensicherheit | ILV
Health Assisting Engineering	1	Elektronische Bauelemente und Digitaltechnik | ILV
Health Assisting Engineering	1	Sensoren und Aktoren | ILV
Health Assisting Engineering	2	Assessments | SE
Health Assisting Engineering	2	Funktionelle Anatomie und Biomechanik | SE
Health Assisting Engineering	2	Exkursion | SE
Health Assisting Engineering	2	Ideen und Innovationen | ILV
Health Assisting Engineering	2	User Experience Design | ILV
Health Assisting Engineering	2	Konzeption von medizinisch-technischen Geräten | ILV
Health Assisting Engineering	2	Mikrocontroller Anwendungen | ILV
Health Assisting Engineering	2	Requirements Engineering | ILV
Health Assisting Engineering	2	Qualitative Methoden der Bedarfsermittlung und Evaluation | ILV
Health Assisting Engineering	2	Quantitative Methoden | ILV
Health Assisting Engineering	2	Clinical Research | SE
Health Assisting Engineering	2	Ethik in der Forschung | ILV
Health Assisting Engineering	2	Exposé | SE
Health Assisting Engineering	3	Ausgewählte Gesprächssettings | UE
Health Assisting Engineering	3	Grundlagen des Innovations-, Technologie- und Produktmanagements | ILV
Health Assisting Engineering	3	Produktrealisierung | SE
Health Assisting Engineering	3	Wissenschaftliches Projektmanagement und Förderwesen | ILV
Health Assisting Engineering	3	Evidence based practice | SE
Health Assisting Engineering	3	Spezifische Rechtsgrundlagen | ILV
Health Assisting Engineering	3	Ausgewählte Befundungs- und Messverfahren | ILV
Health Assisting Engineering	3	Current topics & Journal club - Bewegungswissenschaft | UE
Health Assisting Engineering	3	Fachspezifische Vertiefung 1 | ILV
Health Assisting Engineering	3	Fachspezifische Vertiefung 2 | ILV
Health Assisting Engineering	3	Current topics & Journal club - Handlungswissenschaften | UE
Health Assisting Engineering	3	Occupational Science | ILV
Health Assisting Engineering	3	Mobile App Development | ILV
Health Assisting Engineering	3	Modellbildung und Simulation | SE
Health Assisting Engineering	3	Gesundheitssysteme und Versorgungsmodelle im internationalen Vergleich | VO
Health Assisting Engineering	3	Prothetik | ILV
Health Assisting Engineering	3	Current Topics in Digital Health | ILV
Health Assisting Engineering	3	Journal Club zu Digital Health | SE
Health Assisting Engineering	3	Telehealth in Theorie und Praxis | ILV
Health Assisting Engineering	4	Ausgewählte Beratungstechniken und -methoden | ILV
Health Assisting Engineering	4	e-counseling und Tele-Health | ILV
Health Assisting Engineering	4	Masterprüfung | AP
Health Assisting Engineering	4	Seminar zur Masterthesis | SE
Health Assisting Engineering	4	Grundlagen der Technikfolgenabschätzung | ILV
Health Studies	1	Studium starten | SE
Health Studies	1	Anatomie | VO
Health Studies	1	Pathologie | VO
Health Studies	1	Physiologie | VO
Health Studies	1	Wissenschaftliches Arbeiten und Literaturrecherche | SE
Health Studies	1	Evidenzbasierte Praxis und Critically Appraised Topic | SE
Health Studies	1	Anamneseerhebung, Kommunikation und Gesprächsführung | ILV
Health Studies	1	Kardio-, Pulmo-, Neurologische Grundlagen | ILV
Health Studies	1	Ergonomie | ILV
Health Studies	1	Patient*innensicherheit und -management | VO
Health Studies	1	PSG: Durchführung, Interpretation & Therapie | ILV
Health Studies	1	Schlaf - Grundlagen | VO
Health Studies	1	Histologie | VO
Health Studies	1	Histologische Morphologie (Physiologie) | ILV
Health Studies	1	Histologische Morphologie (Pathologie) | ILV
Health Studies	1	Makroskopische Anatomie, Materialgewinnung und Diagnostik | VO
Health Studies	1	Standardisierte Zuschnitt- und Dokumentationsverfahren | ILV
Health Studies	1	Grundlagen der histologischer Techniken, Präanalytik und Färbeverfahren | ILV
Health Studies	1	Grundlagen von IHC & ISH inklusive Dokumentation und Qualitätssicherung | SE
Health Studies	1	Einführung in die Digitale Pathlogie/ Zytologie und Whole-Slide-Imaging | VO
Health Studies	1	IT- und AI-Grundlagen in der Digitalen Pathologie/ Zytologie | ILV
Health Studies	1	Grundlagen der gynäkologischen Zytologie & Zytomorphologie | ILV
Health Studies	1	Klassifikation, Befundpraxis & Troubleshooting | ILV
Health Studies	1	Grundlagen & Zytologie der Lunge und Harntraktzytologie | ILV
Health Studies	1	Zytologie der serösen Höhlen, Zysten & Gelenksergüsse | ILV
Health Studies	1	Präanalytische Grundlagen | VO
Health Studies	1	Aufbereitungsmethoden & Färbetechniken | ILV
Health Studies	2	Berufsbild und Gesetzlicher Rahmen | VO
Health Studies	2	Biomedizinischer Befund in der Funktionsdiagnostik | VO
Health Studies	2	Ethik in der Funktionsdiagnostik | VO
Health Studies	2	Forschung in der Funktionsdiagnostik | VO
Health Studies	2	Notfallmanagement und Erste Hilfe | ILV
Health Studies	2	Pharmakologie | VO
Health Studies	2	Fallstudien | SE
Health Studies	2	Weiterführende kardiologische Untersuchungen | ILV
Health Studies	2	Weiterführende neurologische Untersuchungen | ILV
Health Studies	2	Weiterführende pulmologische Untersuchungen | ILV
Health Studies	2	Scoring, weiterführende Untersuchungen | ILV
Health Studies	2	Digitale Pathologie - vertiefend (Hands-on mit Python & AI) | ILV
Health Studies	2	Zuschnittsystematik - vertiefend | ILV
Health Studies	2	Grundlagen der molekularen Onkologie und angewandte molekularpathologische Methoden | ILV
Health Studies	2	Interpretation und Troubleshooting molekularpathologischer Ergebnisse | SE
Health Studies	2	Berufsbild und rechtliche Grundlagen in der Histopathologie und Zytologie | ILV
Health Studies	2	Zytologie & Histopathologie: Internationale Standards, Freiberuflichkeit und Schnittstellenmanagement | SE
Health Studies	2	Datenauswertung in der Pathologie | SE
Health Studies	2	Forschungsprojekt-Simulation "Von der Idee zur Studie" | SE
Health Studies	2	Journal Club Pathologie/ Zytologie | SE
Health Studies	2	Theoretische Grundlagen Casemanagement | SE
Health Studies	2	Intra- und Interprofessionelle Case studies | SE
Health Studies	2	Qualitätsmanagement in der histo- und zytologischen Diagnostik: Von der Akkreditierung zur digitalen Absicherung | ILV
Health Studies	2	Spezialverfahren der Geweberverarbeitung und Mikrotomie | ILV
Health Studies	2	Validierung, Verifizierung und diagnostische Markersteuerung in der Histopathologie | SE
Health Studies	2	Integrierte Molekulardiagnostik | ILV
Health Studies	2	Befunderstellung & Tumorboard-Praxis | ILV
Health Studies	2	Komplexe dysplastische Zellbilder & Analzytologie | ILV
Health Studies	2	Glanduläre Zellbilder | ILV
Health Studies	2	Zytologie-Histologie-Kolposkopie-Molekularpathologie | SE
Health Studies	2	Zytologie der Schilddrüse & Brustdrüse | ILV
Health Studies	2	Zytologie des Gastrointestinaltrakts, Leber & Pankreas | ILV
Health Studies	2	Liquorzytologie | ILV
Health Studies	2	Lymphknoten & Knochenmarkzytologie & Zytologie des Auges | ILV
Health Studies	2	Troubleshooting in der Präanalytik & Forensische Zytologie | SE
Health Studies	2	Fallmanagement, Befundkorrelation & Ableitung von Maßnahmen | SE
Health Studies	2	Troubleshooting in der Präanalytik | SE
Health Studies	2	Fallmanagement und ROSE-basierte Entscheidungsfindung | SE
Health Studies	2	Erweiterte immunzytochemische Diagnostik und molekulare Diagnostik spezieller Fragestellungen | VO
Health Studies	2	Immunzytochemie in der Zytologie (PAP, Dünnschicht, EBUS, FNA) | SE
Health Studies	3	Qualitäts-, Risiko- und Lean Management im Gesundheitswesen | SE
Health Studies	3	Strategisches Organisations- und Change Management | SE
Health Studies	3	Führung, Selbst- und Teammanagement | SE
Health Studies	3	Krisenmanagement und Resilienzstrategien in Organisationen | SE
Health Studies	3	Leadership und Organisationskultur | SE
Health Studies	3	Managementsysteme und Governance in der Praxis | UE
Health Studies	3	Nachhaltigkeit, Ökonomie und strategische Steuerung | SE
Health Studies	3	Auswahl und Implementierung digitaler Lösungen | ILV
Health Studies	3	eHealth, Künstliche Intelligenz und digitale Versorgungssysteme im Gesundheitswesen | ILV
Health Studies	3	Gesundheitsförderung und Prävention | VO
Health Studies	3	Gesundheitssysteme und Gesundheitsökonomie | VO
Health Studies	3	Konzepte interprofessioneller Gesundheitsversorgung (Care & Case Management) | VO
Health Studies	3	Praxislabor | SE
Health Studies	3	Implementierung von Evidenz in Entscheidungs- und Versorgungsprozessen | ILV
Health Studies	3	Qualitative Forschung und Mixed-Methods | ILV
Health Studies	3	Quantitative Forschung | ILV
Health Studies	3	Systematische Literaturrecherche und Bewertung der Studienqualität | ILV
Health Studies	4	Internationale Professionalisierungsmodelle und berufliche Identität | ILV
Health Studies	4	Interprofessionelle Verantwortung und partizipative Entscheidungsfindung | ILV
Health Studies	4	Professionelles Handeln, Awareness und Multiplikator*innenrolle | SE
Health Studies	4	Recht, Berufsethik, Berufspolitik und Verantwortung im Gesundheitswesen | VO
Health Studies	4	Expose | SE
Health Studies	4	Masterprüfung | AP
Health Studies	4	Seminar zur Masterthesis | SE
Health Tech and Clinical Engineering	1	Mathematische und statistische Methoden im medizinischen Umfeld | ILV
Health Tech and Clinical Engineering	1	Softwareentwicklung für medizinische Systeme | ILV
Health Tech and Clinical Engineering	1	Biomedizinische Signalverarbeitung | ILV
Health Tech and Clinical Engineering	1	Cybersecurity für IT-Systeme und Medizingeräte im Gesundheitswesen | ILV
Health Tech and Clinical Engineering	1	Anwendungen von MedDev für die Softwareentwicklung | ILV
Health Tech and Clinical Engineering	1	Entwicklungsstandards und -methoden für medizinische Software | ILV
Health Tech and Clinical Engineering	1	Medizintechnische Geräte und Anwendungen | ILV
Health Tech and Clinical Engineering	1	Sicherheitsbewertungen und Risikoanalyse in der Medizintechnik | ILV
Health Tech and Clinical Engineering	1	Public Health Policy and Regulatory Affairs | ILV
Health Tech and Clinical Engineering	2	Umwelt- und Energiemanagement in Gesundheitseinrichtungen | ILV
Health Tech and Clinical Engineering	2	Intelligente Systeme in der Gebäudeautomatisierung | ILV
Health Tech and Clinical Engineering	2	Regulatorische Anforderungen und Normen in der Gebäudetechnik | ILV
Health Tech and Clinical Engineering	2	AI and Robotics in Medical Systems | ILV
Health Tech and Clinical Engineering	2	Health Data Science Structures | ILV
Health Tech and Clinical Engineering	2	Medical Data Science Analysis | ILV
Health Tech and Clinical Engineering	2	Entwicklungszyklen von medizinischen Geräten | ILV
Health Tech and Clinical Engineering	2	Entrepreneurship und Innovationsmanagement | ILV
Health Tech and Clinical Engineering	2	Management und Führungskompetenzen | ILV
Health Tech and Clinical Engineering	3	Trends und Zukunftstechnologien | SE
Health Tech and Clinical Engineering	3	Wahlpflichtfach | ILV
Health Tech and Clinical Engineering	3	Predictive Analytics in Health Care | ILV
Health Tech and Clinical Engineering	3	Critical Thinking and Ethical Challenges in Science and Health | SE
Health Tech and Clinical Engineering	3	Implementierung und Monitoring von Qualitätssicherungsprozessen | ILV
Health Tech and Clinical Engineering	3	Medical Device Regulatory Framework | ILV
Health Tech and Clinical Engineering	3	Telemedizin und digitale Innovationen | ILV
Health Tech and Clinical Engineering	3	VR-gestütztes Training und Simulation in der Medizin | ILV
Health Tech and Clinical Engineering	4	Master Thesis Seminar | SE
Health Tech and Clinical Engineering	4	Masterprüfung | AP
Hebammen	1	Organisation von Theorie und Praxis im Hebammenstudium | SE
Hebammen	1	Einführung in wissenschaftliches Arbeiten | SE
Hebammen	1	Medical English | SE
Hebammen	1	Anatomie | VO
Hebammen	1	Physiologie | VO
Hebammen	1	Hygiene | VO
Hebammen	1	Embryologie | VO
Hebammen	1	Die physiologisch verlaufende Schwangerschaft | ILV
Hebammen	1	Pflegerische Grundlagen der Hebammentätigkeit | ILV
Hebammen	1	Konzepte beruflicher Kommunikation | SE
Hebammen	1	Die physiologisch verlaufende Neugeborenenperiode | ILV
Hebammen	1	Der physiologische Wochenbettverlauf | ILV
Hebammen	1	Stillen und Stillberatung | ILV
Hebammen	1	Geschichte des Hebammenberufes | SE
Hebammen	1	Rechtliche Grundlagen der Gesundheitsberufe | VO
Hebammen	1	Berufsbild und Berufsidentität | SE
Hebammen	1	Selbstfürsorge und Selbstwirksamkeit | SE
Hebammen	1	Praxisbegleitseminar mit Praxistraining 1 | ILV
Hebammen	2	Gesundheitsmodelle und Gesundheitsförderung | SE
Hebammen	2	Konzepte der Soziologie, Psychologie und Pädagogik | VO
Hebammen	2	Beratung und Betreuung in komplexen Lebenssituationen 1 | SE
Hebammen	2	Grundlagen der Gesundheitsforschung | SE
Hebammen	2	Allgemeine Pathologie | VO
Hebammen	2	Fachspezifische Mikrobiologie | VO
Hebammen	2	Sexualität und Familienplanung | ILV
Hebammen	2	Assistierte Reproduktion und Pränataldiagnostik | SE
Hebammen	2	Ethik im Gesundheitswesen | SE
Hebammen	2	Schwangerenvorsorge | ILV
Hebammen	2	Ernährungslehre und Diätetik | ILV
Hebammen	2	Der physiologische Geburtsverlauf | ILV
Hebammen	2	Überwachung und Dokumentation | ILV
Hebammen	2	Abweichungen vom physiologischen Verlauf | ILV
Hebammen	2	Praxisbegleitseminar 2 | ILV
Hebammen	2	Praxistraining 2 | UE
Hebammen	3	Frauen*Gesundheitsforschung | SE
Hebammen	3	Angewandte Hebammenforschung | ILV
Hebammen	3	Scientific English | ILV
Hebammen	3	Strategien beruflicher Konfliktlösung | ILV
Hebammen	3	Gynäkologie | VO
Hebammen	3	Neonatologie | VO
Hebammen	3	Allgemeine und spezielle Pharmakologie | VO
Hebammen	3	Geburtshilfliche Analgesie und Anästhesie | VO
Hebammen	3	Geburtsmedizinische Komplikationen 1 | VO
Hebammen	3	Pathologische Verläufe in der Geburtshilfe 1 | ILV
Hebammen	3	Praxistraining 3 | UE
Hebammen	3	Praxisbegleitseminar 3 | ILV
Hebammen	4	Beratung und Betreuung in der Schwangerschaft | ILV
Hebammen	4	Konzepte und Methoden der Geburtsvorbereitung | ILV
Hebammen	4	Außerklinische Geburtshilfe | ILV
Hebammen	4	Wissenschaftliches Lesen und Schreiben | ILV
Hebammen	4	Stillen in komplexen Situationen | ILV
Hebammen	4	Bewegungsförderung vor und nach der Geburt | ILV
Hebammen	4	Beratung und Betreuung zu Säuglingsalter und Elternschaft | ILV
Hebammen	4	Pädiatrie im Kleinkindalter | VO
Hebammen	4	Geburtsmedizinische Komplikationen 2 | VO
Hebammen	4	Pathologische Verläufe in der Geburtshilfe 2 | ILV
Hebammen	4	Notfallmanagement | SE
Hebammen	4	Einführung in die Fetalsonographie | ILV
Hebammen	4	Chirurgische Wundversorgung von Geburtsverletzungen | ILV
Hebammen	4	Praxistraining 4 | UE
Hebammen	4	Praxisbegleitseminar 4 | ILV
Hebammen	5	Rechtliche Grundlagen des Hebammenberufes | VO
Hebammen	5	Organisation und Betriebsmanagement | SE
Hebammen	5	Qualitätsmanagement | ILV
Hebammen	5	Beratung und Betreuung in komplexen Lebenssituationen 2 | ILV
Hebammen	5	Begleitseminar zur Bachelorarbeit Teil 1 | SE
Hebammen	5	Clinical Reasoning | ILV
Hebammen	5	Praxisbegleitseminar 5 | ILV
Hebammen	5	Praxistraining 5 | UE
Hebammen	6	Midwifery in an international or multidisciplinary context a | ILV
Hebammen	6	Midwifery in an international or multidisciplinary context b | ILV
Hebammen	6	Midwifery in an international or multidisciplinary context c | ILV
Hebammen	6	Midwifery in an international or multidisciplinary context d | ILV
Hebammen	6	Current Issues | SE
Hebammen	6	Diversität und Diversitätskompetenz | SE
Hebammen	6	Außerklinische Geburtshilfe und Familienbegleitung - Fachvertiefung | ILV
Hebammen	6	Stillberatung für Fortgeschrittene | ILV
Hebammen	6	Beckenbodengesundheit aus multiprofessioneller Perspektive | ILV
Hebammen	6	Konzepte der Komplementärmedizin | SE
Hebammen	6	Traumasensible Betreuung während Schwangerschaft, Geburt und Wochenbett | SE
Hebammen	6	Angewandte Ethik für Health Professionals | SE
Hebammen	6	Soziale Arbeit in der Familienbetreuung | SE
Hebammen	6	Klimakompetenz für Health Professionals | SE
Hebammen	6	KI, Insta & Co. - Digitalisierung für Health Professionals | ILV
Hebammen	6	Begleitseminar zur Bachelorarbeit Teil 2 | SE
Hebammen	6	Bachelorprüfung | AP
Hebammen	6	Praxisbegleitseminar 6 | ILV
Hebammen	6	Praxistraining 6 | UE
High Tech Manufacturing	1	Grundlegende Bearbeitungsmethoden in der Fertigungstechnik | ILV
High Tech Manufacturing	1	Mechanische Werkstätte | UE
High Tech Manufacturing	1	Höhere Mathematik für Ingenieur*innen | ILV
High Tech Manufacturing	1	Einführung in die Informatik | ILV
High Tech Manufacturing	1	Grundlagen der Elektronik und Elektrotechnik I | ILV
High Tech Manufacturing	1	Mechanik | ILV
High Tech Manufacturing	1	Chemie der Werkstoffe | VO
High Tech Manufacturing	1	Einführung in die Geometrie von Konstruktionen | ILV
High Tech Manufacturing	1	Einführung in die Kraftfahrzeugtechnik | VO
High Tech Manufacturing	1	Grundlagen der Werkstoffkunde, Spezifikationen, Eigenschaften, Einsatzgebiete | VO
High Tech Manufacturing	1	Soziale Kompetenzen I | SE
High Tech Manufacturing	2	High Tech Fertigungsverfahren | VO
High Tech Manufacturing	2	Angewandte Physik | ILV
High Tech Manufacturing	2	Grundlagen Maschinenelemente | ILV
High Tech Manufacturing	2	Angewandte Differential- und Integralrechnung | ILV
High Tech Manufacturing	2	Elektroniklabor | UE
High Tech Manufacturing	2	Grundlagen der Elektronik und Elektrotechnik II | ILV
High Tech Manufacturing	2	Sensorik und Aktorik | VO
High Tech Manufacturing	2	Einführung Mikrocontroller-Programmierung | ILV
High Tech Manufacturing	2	Konstruktionsübungen | ILV
High Tech Manufacturing	2	Soziale Kompetenzen II | SE
High Tech Manufacturing	2	Werkstoffprüfverfahren | ILV
High Tech Manufacturing	3	Festigkeitslehre und finite Elemente Methoden | VO
High Tech Manufacturing	3	Übung zu Festigkeitslehre | UE
            """.trimIndent()
        )
        append('\n')
        append(
            """
High Tech Manufacturing	3	Einführung in die Logistik | ILV
High Tech Manufacturing	3	Ethik und Nachhaltigkeit | SE
High Tech Manufacturing	3	Grundlagen der Betriebswirtschaft | VO
High Tech Manufacturing	3	Fertigungsplanung CAD/CAM | ILV
High Tech Manufacturing	3	Produktionsmanagement | ILV
High Tech Manufacturing	3	Steuerungssysteme | ILV
High Tech Manufacturing	3	Konstruktionsprojekt | SE
High Tech Manufacturing	3	Anwendung höherer Mathematik | ILV
High Tech Manufacturing	3	Maschinen-, Werkzeug- und Vorrichtungsbau | ILV
High Tech Manufacturing	3	Statistik und Wahrscheinlichkeitsrechnung | ILV
High Tech Manufacturing	4	Kostenrechnung und Controlling | ILV
High Tech Manufacturing	4	Logistik in der High Tech Industrie | ILV
High Tech Manufacturing	4	Fabriksimulation | ILV
High Tech Manufacturing	4	Mess- und Regelungstechnik | ILV
High Tech Manufacturing	4	Roboter und Handhabungstechnik | ILV
High Tech Manufacturing	4	Einführung in Interdisziplinäre Projekte | ILV
High Tech Manufacturing	4	Einführung in wissenschaftliches Arbeiten für ausgewählte Forschungsfragen | SE
High Tech Manufacturing	4	Projektmanagement in interdisziplinären Projekten | SE
High Tech Manufacturing	4	Prozesse in der Produktentwicklung | VO
High Tech Manufacturing	4	Einführung Additive Manufacturing Technologies | ILV
High Tech Manufacturing	4	Recyclingtechnologien | ILV
High Tech Manufacturing	4	Thermodynamik | ILV
High Tech Manufacturing	5	Digitale Zwillinge | ILV
High Tech Manufacturing	5	Virtual Reality und Augmented Reality | SE
High Tech Manufacturing	5	English Presentations for Experts | UE
High Tech Manufacturing	5	Praxisbegleitung | SE
High Tech Manufacturing	5	Berufspraktikum (575 Stunden) | PR
High Tech Manufacturing	6	Automatisierte Fertigungssysteme | ILV
High Tech Manufacturing	6	Automatisierungslabor | UE
High Tech Manufacturing	6	Einführung in KI / Machine-Learning | VO
High Tech Manufacturing	6	Präsentation und Moderation | UE
High Tech Manufacturing	6	Enterprise Resource Planning Systems | ILV
High Tech Manufacturing	6	Grundlagen des Rechts, Gesellschaftsrecht und Umweltrecht | VO
High Tech Manufacturing	6	Leittechnik | VO
High Tech Manufacturing	6	Manufacturing Execution Systems | ILV
High Tech Manufacturing	6	Qualitätsmanagement | ILV
High Tech Manufacturing	6	Entrepreneurship | ILV
High Tech Manufacturing	6	Innovationsmanagement | ILV
High Tech Manufacturing	6	Marketing und Verkauf | ILV
High Tech Manufacturing	6	Bachelorarbeit | SE
High Tech Manufacturing	6	Bachelorprüfung | AP
Histopathologie	1	Studium starten | SE
Histopathologie	1	Anatomie | VO
Histopathologie	1	Pathologie | VO
Histopathologie	1	Histologie | VO
Histopathologie	1	Wissenschaftliches Arbeiten und Literaturrecherche | SE
Histopathologie	1	Evidenzbasierte Praxis und Critically Appraised Topic | SE
Histopathologie	1	Histologische Morphologie (Physiologie) | ILV
Histopathologie	1	Histologische Morphologie (Pathologie) | ILV
Histopathologie	1	Makroskopische Anatomie, Materialgewinnung und Diagnostik | VO
Histopathologie	1	Standardisierte Zuschnitt- und Dokumentationsverfahren | ILV
Histopathologie	1	Grundlagen der histologischer Techniken, Präanalytik und Färbeverfahren | ILV
Histopathologie	1	Grundlagen von IHC & ISH inklusive Dokumentation und Qualitätssicherung | SE
Histopathologie	1	Einführung in die Digitale Pathlogie/ Zytologie und Whole-Slide-Imaging | VO
Histopathologie	1	IT- und AI-Grundlagen in der Digitalen Pathologie/ Zytologie | ILV
Histopathologie	2	Berufsbild und rechtliche Grundlagen in der Histopathologie und Zytologie | ILV
Histopathologie	2	Zytologie & Histopathologie: Internationale Standards, Freiberuflichkeit und Schnittstellenmanagement | SE
Histopathologie	2	Qualitätsmanagement in der histo- und zytologischen Diagnostik: Von der Akkreditierung zur digitalen Absicherung | ILV
Histopathologie	2	Grundlagen der molekularen Onkologie und angewandte molekularpathologische Methoden | ILV
Histopathologie	2	Interpretation und Troubleshooting molekularpathologischer Ergebnisse | SE
Histopathologie	2	Digitale Pathologie - vertiefend (Hands-on mit Python & AI) | ILV
Histopathologie	2	Datenauswertung in der Pathologie | SE
Histopathologie	2	Forschungsprojekt-Simulation "Von der Idee zur Studie" | SE
Histopathologie	2	Journal Club Pathologie/ Zytologie | SE
Histopathologie	2	Zuschnittsystematik - vertiefend | ILV
Histopathologie	2	Theoretische Grundlagen Casemanagement | SE
Histopathologie	2	Intra- und Interprofessionelle Case studies | SE
Histopathologie	2	Spezialverfahren der Geweberverarbeitung und Mikrotomie | ILV
Histopathologie	2	Validierung, Verifizierung und diagnostische Markersteuerung in der Histopathologie | SE
Histopathologie	2	Integrierte Molekulardiagnostik | ILV
Histopathologie	2	Befunderstellung & Tumorboard-Praxis | ILV
IT-Security	1	Identity and Access Management | ILV
IT-Security	1	Network Security | ILV
IT-Security	1	Applied Cryptography | ILV
IT-Security	1	Advanced Project Management | ILV
IT-Security	1	Secure Software Engineering | ILV
IT-Security	1	IT-Security Project I | ILV
IT-Security	2	DevSecOps | ILV
IT-Security	2	IT-Security Project II | ILV
IT-Security	2	IoT and OT Security | ILV
IT-Security	2	Operating Systems Security | ILV
IT-Security	2	AI and Security | ILV
IT-Security	2	Complex Problem Solving | ILV
IT-Security	2	Information Security Management | ILV
IT-Security	3	Master Thesis Project | ILV
IT-Security	3	Deep Learning | ILV
IT-Security	3	Digital Leadership | ILV
IT-Security	3	Distributed Ledger Technologies | ILV
IT-Security	3	Game Engineering | ILV
IT-Security	3	Advanced Embedded and IoT Security | ILV
IT-Security	3	Digital Forensics & Incident Response | ILV
IT-Security	3	Emerging Technologies and Future Threats | ILV
IT-Security	3	Penetration Testing | ILV
IT-Security	3	Mobile App Development | ILV
IT-Security	3	User Centered Design | ILV
IT-Security	3	Web Engineering | ILV
IT-Security	4	Entrepreneurship | ILV
IT-Security	4	Legal IT Aspects | ILV
IT-Security	4	Master Examination | AP
IT-Security	4	Master Thesis Seminar | SE
Integriertes Risikomanagement	1	Grundlagen Governance, Risiko und Organisation | ILV
Integriertes Risikomanagement	1	Weiterführende Grundlagen Governance, Risiko und Organisation | ILV
Integriertes Risikomanagement	1	KI in Theorie und Praxis | ILV
Integriertes Risikomanagement	1	Praxistransfer Positionierung und Kontextanalyse | UE
Integriertes Risikomanagement	1	Reflexion Positionierung und Kontextanalyse | SE
Integriertes Risikomanagement	1	Grundlagen Betriebswirtschaftslehre | ILV
Integriertes Risikomanagement	1	Weiterführende Betriebswirtschaftslehre | VO
Integriertes Risikomanagement	1	Grundlagen Prozess- und Qualitätsmanagement | ILV
Integriertes Risikomanagement	1	Weiterführendes Prozess- und Qualitätsmanagement | ILV
Integriertes Risikomanagement	2	Ideenfindung Forschungsprojekt Masterarbeit | ILV
Integriertes Risikomanagement	2	Komplexe Systeme | ILV
Integriertes Risikomanagement	2	Grundlagen Risikotypologien | ILV
Integriertes Risikomanagement	2	Weiterführende Risikotypologien | ILV
Integriertes Risikomanagement	2	Grundlagen Enterprise Risk Management | ILV
Integriertes Risikomanagement	2	Weiterführendes Enterprise Risk Management | ILV
Integriertes Risikomanagement	2	Praxistransfer Steuerung und Verantwortung in komplexen Situationen | UE
Integriertes Risikomanagement	2	Reflexion Steuerung und Verantwortung in komplexen Situationen | SE
Integriertes Risikomanagement	3	Forschungsmethoden | ILV
Integriertes Risikomanagement	3	Forschungsprojekt Masterarbeit - Konzeption | SE
Integriertes Risikomanagement	3	Praxistransfer Entwicklung und Innovation im Berufsfeld | UE
Integriertes Risikomanagement	3	Reflexion Entwicklung und Innovation im Berufsfeld | SE
Integriertes Risikomanagement	3	Audits | UE
Integriertes Risikomanagement	3	Risikotrends und Zukunftsthemen | ILV
Integriertes Risikomanagement	3	Soziale Systeme | VO
Integriertes Risikomanagement	3	Vitale Führung | UE
Integriertes Risikomanagement	3	Interne Revision | ILV
Integriertes Risikomanagement	3	Weiterführende Interne Revision | ILV
Integriertes Risikomanagement	3	Grundlagen Security Governance | ILV
Integriertes Risikomanagement	3	Weiterführende Security Governance | VO
Integriertes Risikomanagement	4	Betreuung Forschungsprojekt Masterarbeit | SE
Integriertes Risikomanagement	4	Integriertes Risikomanagement | SE
Integriertes Risikomanagement	4	Masterprüfung | AP
Integriertes Sicherheitsmanagement	1	Grundlagen Risiko und Statistik | ILV
Integriertes Sicherheitsmanagement	1	Weiterführendes Risiko und Statistik | ILV
Integriertes Sicherheitsmanagement	1	Grundlagen Prozess- und Qualitätsmanagement | ILV
Integriertes Sicherheitsmanagement	1	Weiterführendes Prozess- und Qualitätsmanagement | ILV
Integriertes Sicherheitsmanagement	1	KI in Theorie und Praxis | ILV
Integriertes Sicherheitsmanagement	1	Wissenschaftliches Denken und Arbeiten | ILV
Integriertes Sicherheitsmanagement	1	Praxistransfer Einstieg in die Berufspraxis | UE
Integriertes Sicherheitsmanagement	1	Reflexion Einstieg in die Berufspraxis | SE
Integriertes Sicherheitsmanagement	2	Grundlagen Risikomanagement | ILV
Integriertes Sicherheitsmanagement	2	Weiterführendes Risikomanagement | ILV
Integriertes Sicherheitsmanagement	2	Grundlagen Technik und Gefahren | ILV
Integriertes Sicherheitsmanagement	2	Weiterführende Technik und Gefahren | ILV
Integriertes Sicherheitsmanagement	2	Brandschutz | ILV
Integriertes Sicherheitsmanagement	2	Projektmanagement | ILV
Integriertes Sicherheitsmanagement	2	Praxistransfer Mitwirken in betrieblichen Prozessen | UE
Integriertes Sicherheitsmanagement	2	Reflexion Mitwirken in betrieblichen Prozessen | SE
Integriertes Sicherheitsmanagement	3	Grundlagen Arbeitnehmer*innenschutz | ILV
Integriertes Sicherheitsmanagement	3	Weiterführender Arbeitnehmer*innenschutz | ILV
Integriertes Sicherheitsmanagement	3	Forschungsmethoden | ILV
Integriertes Sicherheitsmanagement	3	Organisationslehre | SE
Integriertes Sicherheitsmanagement	3	Praxistransfer Verantwortungsübernahme und Professionalisierung | UE
Integriertes Sicherheitsmanagement	3	Reflexion Verantwortungsübernahme und Professionalisierung | SE
Integriertes Sicherheitsmanagement	4	Grundlagen Security Management & Business Continuity Management | ILV
Integriertes Sicherheitsmanagement	4	Weiterführendes Security Management & Business Continuity Management | ILV
Integriertes Sicherheitsmanagement	4	Grundlagen Informationssicherheit | ILV
Integriertes Sicherheitsmanagement	4	Weiterführende Informationssicherheit | ILV
Integriertes Sicherheitsmanagement	4	Grundlagen Physische Sicherheit | ILV
Integriertes Sicherheitsmanagement	4	Weiterführende Physische Sicherheit | ILV
Integriertes Sicherheitsmanagement	4	Betreuung Konzeption Forschungsprojekt Bachelorarbeit | SE
Integriertes Sicherheitsmanagement	4	Konzeption Forschungsprojekt Bachelorarbeit | SE
Integriertes Sicherheitsmanagement	4	Praxistransfer Koordination und Schnittstellenmanagement | UE
Integriertes Sicherheitsmanagement	4	Reflexion Koordination und Schnittstellenmanagement | UE
Integriertes Sicherheitsmanagement	5	IT-/OT-Sicherheit | VO
Integriertes Sicherheitsmanagement	5	Krisenübung | UE
Integriertes Sicherheitsmanagement	5	Organisationsentwicklung, Beratung und Krisenmanagement | ILV
Integriertes Sicherheitsmanagement	5	Betreuung Forschungsprojekt Bachelorarbeit - Operationalisierung | SE
Integriertes Sicherheitsmanagement	5	Forschungsprojekt Bachelorarbeit - Operationalisierung | SE
Integriertes Sicherheitsmanagement	5	Praxistransfer Handeln unter Belastungs- und Leistungsanforderungen | ILV
Integriertes Sicherheitsmanagement	5	Reflexion Praxistransfer Handeln unter Belastungs- und Leistungsanforderungen | UE
Integriertes Sicherheitsmanagement	5	Brandschutz | ILV
Integriertes Sicherheitsmanagement	5	Soziale Systeme | ILV
Integriertes Sicherheitsmanagement	5	Umwelt- und Abfallmanagement | ILV
Integriertes Sicherheitsmanagement	5	Vitale Führung | ILV
Integriertes Sicherheitsmanagement	6	Integriertes Sicherheitsmanagement | ILV
Integriertes Sicherheitsmanagement	6	Bachelorprüfung | AP
Integriertes Sicherheitsmanagement	6	Bachelorprüfung
Integriertes Sicherheitsmanagement	6	Forschungsprojekt Bachelorarbeit - Finalisierung | UE
Integriertes Sicherheitsmanagement	6	Betreuung Forschungsprojekt Bachelorarbeit - Finalisierung | SE
Integriertes Sicherheitsmanagement	6	Betreuung Praktikum | SE
Integriertes Sicherheitsmanagement	6	Praktikum | PR
Kinder- und Familienzentrierte Soziale Arbeit	1	Besonderheiten psychosozialer kindlicher Entwicklung | VO
Kinder- und Familienzentrierte Soziale Arbeit	1	Konzepte biopsychosozialer Gesundheit | VO
Kinder- und Familienzentrierte Soziale Arbeit	1	Übungen zu Konzepten biopsychosozialer Gesundheit | UE
Kinder- und Familienzentrierte Soziale Arbeit	1	Psychosoziale Diagnostik und Indikation | ILV
Kinder- und Familienzentrierte Soziale Arbeit	1	Übungen zu psychosozialer Diagnostik und Indikation | UE
Kinder- und Familienzentrierte Soziale Arbeit	1	Gesprächsführung in Mehrpersonensettings | UE
Kinder- und Familienzentrierte Soziale Arbeit	1	Psychosoziale Kommunikation und Beratung | ILV
Kinder- und Familienzentrierte Soziale Arbeit	1	Qualitative Forschungsmethoden | VO
Kinder- und Familienzentrierte Soziale Arbeit	1	Quantitative Forschungsmethoden | VO
Kinder- und Familienzentrierte Soziale Arbeit	1	Wissenschaftliches Schreiben | SE
Kinder- und Familienzentrierte Soziale Arbeit	2	Psychosoziale Diagnostik und Interventionsplanung | ILV
Kinder- und Familienzentrierte Soziale Arbeit	2	Übungen zu Psychosozialer Diagnostik und Interventionsplanung | UE
Kinder- und Familienzentrierte Soziale Arbeit	2	Gesprächsführung mit Kindern und Jugendlichen | UE
Kinder- und Familienzentrierte Soziale Arbeit	2	Motivierende und aktivierende Gesprächsführung | UE
Kinder- und Familienzentrierte Soziale Arbeit	2	Besonderheiten in der transkulturellen Arbeit mit Familien | ILV
Kinder- und Familienzentrierte Soziale Arbeit	2	Migration und Familie | ILV
Kinder- und Familienzentrierte Soziale Arbeit	2	Forschungsfelder zur Konzeptentwicklung | SE
Kinder- und Familienzentrierte Soziale Arbeit	2	Masterarbeit - Forschungsdesign | UE
Kinder- und Familienzentrierte Soziale Arbeit	2	Praxisforschung | SE
Kinder- und Familienzentrierte Soziale Arbeit	2	Spezifische Ansätze der sozialpädagogischen Arbeit | ILV
Kinder- und Familienzentrierte Soziale Arbeit	2	Armut und sozialer Ausschluss von Familien | ILV
Kinder- und Familienzentrierte Soziale Arbeit	2	Rechtliche Rahmenbedingungen sozialtherapeutischer Sozialer Arbeit | VO
Kinder- und Familienzentrierte Soziale Arbeit	3	Risk assessment | ILV
Kinder- und Familienzentrierte Soziale Arbeit	3	Krisenintervention und Deeskalationstechnik | UE
Kinder- und Familienzentrierte Soziale Arbeit	3	Pädagogisch-sozialtherapeutische Arbeit mit Kindern und Familien | VO
Kinder- und Familienzentrierte Soziale Arbeit	3	Professionelles Handeln im Zwangskontext | UE
Kinder- und Familienzentrierte Soziale Arbeit	3	Übungen zu pädagogisch-sozialtherapeutischer Arbeit mit Kindern und Familien | UE
Kinder- und Familienzentrierte Soziale Arbeit	3	Masterarbeit
Kinder- und Familienzentrierte Soziale Arbeit	3	Masterarbeit - Erstellung 1 | SE
Kinder- und Familienzentrierte Soziale Arbeit	3	Ansätze der sozialtherapeutischen Arbeit | VO
Kinder- und Familienzentrierte Soziale Arbeit	3	Ethische Rahmenbedingungen sozialtherapeutischer Sozialer Arbeit | VO
Kinder- und Familienzentrierte Soziale Arbeit	3	Theorieansätze zur Traumapädagogik | ILV
Kinder- und Familienzentrierte Soziale Arbeit	3	Übungen zu traumaintegrierendem professionellem Handeln | UE
Kinder- und Familienzentrierte Soziale Arbeit	4	Diagnostisches Fallverstehen in komplexen Familiensystemen | UE
Kinder- und Familienzentrierte Soziale Arbeit	4	Sozialpädagogische Familienarbeit | VO
Kinder- und Familienzentrierte Soziale Arbeit	4	Übungen zu sozialpädagogischer Familienarbeit im Rahmen von Fremdunterbringungsprozessen | UE
Kinder- und Familienzentrierte Soziale Arbeit	4	Masterarbeit
Kinder- und Familienzentrierte Soziale Arbeit	4	Masterarbeit - Erstellung 2 | SE
Kinder- und Familienzentrierte Soziale Arbeit	4	Masterkolloquium | AP
Kinder- und Familienzentrierte Soziale Arbeit	4	Didaktik der Elternbildung und professionelles Handeln im Sozialraum | SE
Kinder- und Familienzentrierte Soziale Arbeit	4	Frühe Hilfen | SE
Kinder- und Familienzentrierte Soziale Arbeit	4	Präventionsansätze und Bildungstheorien im Familienkontext | VO
Kinder- und Familienzentrierte Soziale Arbeit	4	Aktueller Diskurs | SE
Kinder- und Familienzentrierte Soziale Arbeit	4	Gutachtenerstellung | SE
Kinder- und Jugendlichenpflege	1	Beratung und Förderung der Gesundheitskompetenz von Familien mit ihrem Kind in unterschiedlichen Altersstufen | ILV
Kinder- und Jugendlichenpflege	1	Ernährung | ILV
Kinder- und Jugendlichenpflege	1	Spezielle Pathologie, Diagnose und Therapie | VO
Kinder- und Jugendlichenpflege	1	Komplementärmedizinische Methoden bei Kindern | VO
Kinder- und Jugendlichenpflege	1	Wissenschaftliches Arbeiten | ILV
Kinder- und Jugendlichenpflege	1	Quantitative Forschungsmethoden und Statistik | ILV
Kinder- und Jugendlichenpflege	1	Qualitative Forschungsmethoden | ILV
Kinder- und Jugendlichenpflege	1	3. Lernort Kinästhetik Infant Handling, Basale Stimulation | UE
Kinder- und Jugendlichenpflege	1	Praktikum 1 | PR
Kinder- und Jugendlichenpflege	2	Neonatologie | VO
Kinder- und Jugendlichenpflege	2	Pflege von Frühgeborenen | ILV
Kinder- und Jugendlichenpflege	2	WAHL-LV1: Integrierte Gesundheitsversorgung | ILV
Kinder- und Jugendlichenpflege	2	WAHL-LV2: Das erkrankte Kind im Setting ICU oder IMCU | ILV
Kinder- und Jugendlichenpflege	2	Das chronisch kranke Kind | ILV
Kinder- und Jugendlichenpflege	2	Das Kind mit Behinderung und das rehabilitativ zu betreuende Kind | ILV
Kinder- und Jugendlichenpflege	2	Das Kind in der psychosozialen Krise | ILV
Kinder- und Jugendlichenpflege	2	Das onkologisch erkrankte Kind | ILV
Kinder- und Jugendlichenpflege	2	Das herzerkrankte Kind und das operierte Kind | ILV
Kinder- und Jugendlichenpflege	2	Das palliativ zu betreuende Kind | ILV
Kinder- und Jugendlichenpflege	2	3. Lernort Notfallmanagement inklusive interprofessionelles SIM Training inklusive spezifische Fertigkeiten | UE
Kinder- und Jugendlichenpflege	2	Praktikum 2 | PR
Kinder- und Jugendlichenpflege	3	Abschlussarbeit zu ausgewählten Handlungsfeldern und Begleitseminar | SE
Kinder- und Jugendlichenpflege	3	3. Lernort Supervision und Familiengespräch | UE
Kinder- und Jugendlichenpflege	3	Praktikum 3 | PR
Kinder- und Jugendlichenpflege	3	Praktikum 4 | PR
Logopädie	1	Einführung in die Berufskunde | VO
Logopädie	1	Ergonomie und Bewegungslehre | ILV
Logopädie	1	Kooperative Kommunikation und Gesprächsführung | ILV
Logopädie	1	Logopädischer Prozess und Qualitätssicherung | ILV
Logopädie	1	Supervision für Gesundheitsberufe | VO
Logopädie	1	Wissenschaftliches Arbeiten | ILV
Logopädie	1	Akustisch-physikalische Grundlagen und Psychoakustik | VO
Logopädie	1	Allgemeine Psychologie und Entwicklungspsychologie | ILV
Logopädie	1	Grundlagen der Linguistik | VO
Logopädie	1	Angewandte Supervision: Psychohygiene und Stressbewältigung | SE
Logopädie	1	Atem, Stimme, Sprechen und Sprache: Erleben und Gestalten | ILV
Logopädie	1	Phänomen Stimme | ILV
Logopädie	1	Sprach- und Sprechentwicklung bei Ein- und Mehrsprachigkeit | ILV
Logopädie	1	Allgemeine und spezielle Anatomie | VO
Logopädie	1	Allgemeine und spezielle Physiologie | VO
Logopädie	1	Pathologie und angewandte Hygiene | ILV
Logopädie	2	Klinische Psychologie und Gesundheitspsychologie | ILV
Logopädie	2	Pädagogik und Didaktik | VO
Logopädie	2	Psycholinguistik | VO
Logopädie	2	Rhythmik in der Logopädie | ILV
Logopädie	2	HNO Heilkunde | ILV
Logopädie	2	Pädiatrie | VO
Logopädie	2	Phoniatrie 1 | VO
Logopädie	2	Zahn-, Mund- und Kieferheilkunde, Kieferorthopädie und Kieferchirurgie | ILV
Logopädie	2	Logopädischer Prozess bei Aussprachestörungen | ILV
Logopädie	2	Logopädie in der Rehabilitation 1 | ILV
Logopädie	2	Logopädischer Prozess bei orofacialen Dysfunktionen | ILV
Logopädie	2	Logopädischer Prozess bei Stimmstörungen 1 | ILV
Logopädie	2	Angewandte Supervision: Konflikt und Eskalation | SE
Logopädie	2	Journal Club & Literature Research | SE
Logopädie	2	Praxisreflexion 1 | SE
Logopädie	2	Theorie-Praxis-Transfer 1: Diagnostik | UE
Logopädie	3	Inklusive Pädagogik/Sonderpädagogik | ILV
Logopädie	3	Neuro- und Patholinguistik | VO
Logopädie	3	Neuropsychologie | ILV
Logopädie	3	Neurologie, Neurochirurgie und Neurorehabilitation | VO
Logopädie	3	Phoniatrie 2 | VO
Logopädie	3	Psychiatrie | VO
Logopädie	3	Logopädischer Prozess bei Sprachentwicklungsstörungen | ILV
Logopädie	3	Logopädischer Prozess bei Störungen des Lesens, Schreibens und Rechnens | ILV
Logopädie	3	Audiologie und Audiometrie 1 | ILV
Logopädie	3	Bewegung und Positionierung in der logopädischen Arbeit | UE
Logopädie	3	Logopädie in der Rehabilitation 2 | ILV
Logopädie	3	Logopädischer Prozess bei Facialisparesen | UE
Logopädie	3	Logopädischer Prozess bei neurogener Dysphagie | ILV
Logopädie	3	Logopädischer Prozess bei peripher-organischen Dysphagien | ILV
Logopädie	3	Logopädischer Prozess bei Stimmstörungen 2 | ILV
Logopädie	3	Angewandte Supervision: Professionelle therapeutische Beziehung | SE
Logopädie	3	Praxisreflexion 2 | SE
Logopädie	3	Theorie-Praxis-Transfer II: Therapie | UE
Logopädie	4	Evidenzbasierte Praxis | VO
Logopädie	4	Logopädisches Projekt | SE
Logopädie	4	Qualitative und quantitative Forschungsmethoden | ILV
Logopädie	4	Schreibwerkstatt | SE
Logopädie	4	Wissenschaftliche Methoden | VO
Logopädie	4	Logopädischer Prozess bei Dysglossien und Nasalitätsstörungen | ILV
Logopädie	4	Logopädischer Prozess bei Störungen des Redeflusses | ILV
Logopädie	4	Logopädischer Prozess: Entwicklung unter erschwerten Bedingungen | ILV
Logopädie	4	Logopädischer Prozess: Kommunikation unter erschwerten Bedingungen | ILV
Logopädie	4	Logopädischer Prozess bei neurogen bedingten Sprachstörungen | ILV
Logopädie	4	Logopädischer Prozess bei neurogen bedingten Sprechstörungen | ILV
Logopädie	4	Neurofunktionelle Systematik in der Logopädie | ILV
Logopädie	4	Audiologie und Audiometrie 2 | ILV
Logopädie	4	Logopädischer Prozess bei audiogen bedingten Sprach- und Sprechstörungen | ILV
Logopädie	4	Pädaudiologie, Hörsysteme und hörverbessernde Implantate | ILV
Logopädie	4	Angewandte Supervision: Chronische Krankheiten/Sterben und Tod | SE
Logopädie	4	Praxisreflexion 3 | SE
Logopädie	4	Theorie-Praxis-Transfer III: Evaluierung 1 | UE
Logopädie	5	Bachelorarbeitsseminar 1 | SE
Logopädie	5	Methodenwerkstatt | SE
Logopädie	5	Scientific English | ILV
Logopädie	5	Bewältigung herausfordernder Situationen im interprofessionellen und internationalen Kontext | SE
Logopädie	5	English for Health Professionals | UE
Logopädie	5	Gesprächsführung und Beratung in der Logopädie | SE
Logopädie	5	Angewandte Supervision: Therapeutische Persönlichkeit | SE
Logopädie	5	Klinisches Üben | UE
Logopädie	5	Praxisvorbereitung und Reflexion 4 | SE
Logopädie	5	Theorie-Praxis-Transfer IV: Evaluierung 2 | UE
Logopädie	5	Logopädischer Prozess in der Geriatrie | ILV
Logopädie	5	Logopädischer Prozess in der Intensivmedizin | ILV
Logopädie	5	Logopädischer Prozess in der Neonatologie | ILV
Logopädie	6	Bachelorprüfung | AP
Logopädie	6	Bachelorarbeitsseminar 2 | SE
Logopädie	6	Aktuelle Themen aus dem Berufsfeld | SE
Logopädie	6	Berufskunde Aufbau | ILV
Logopädie	6	Betriebswirtschaftslehre und Selbstständigkeit | VO
Logopädie	6	Ethik und Diversität | ILV
Logopädie	6	Gesundheitsförderung und Prävention in der Logopädie | VO
Logopädie	6	Gesundheitsökonomie und Public Health | VO
Logopädie	6	Recht | VO
Logopädie	6	Angewandte Supervision: Fallarbeit | SE
Logopädie	6	Praxisvorbereitung und Reflexion 5 | SE
Logopädie	6	Theorie-Praxis-Transfer V: Dokumentation | UE
Logopädie	6	Wahlpflichtbereich: Innovationsseminar | SE
Logopädie	6	Wahlpflichtbereich: Praxis | SE
Logopädie	6	Logopädie im speziellen klinischen Kontext | ILV
Logopädie	6	Logopädie im gesundheitsfördernden und präventiven Kontext | ILV
Logopädie	6	Logopädie im interprofessionellen Kontext | ILV
Molecular Biotechnology	1	Bioinformatics & AI | ILV
Molecular Biotechnology	1	Bioethics | ILV
Molecular Biotechnology	1	Interdisciplinary and Intercultural Team Dynamics | ILV
Molecular Biotechnology	1	Scientific Communication I | ILV
Molecular Biotechnology	1	Clinical Drug Development | ILV
Molecular Biotechnology	1	Medical Genetics | VO
Molecular Biotechnology	1	Molecular Genetics | VO
Molecular Biotechnology	1	RNA | VO
Molecular Biotechnology	1	Medical Genetics Lab | UE
Molecular Biotechnology	1	RNA Analysis Lab | UE
Molecular Biotechnology	1	Molecular Immunology | VO
Molecular Biotechnology	1	General Pathology | VO
Molecular Biotechnology	1	Molecular Pathology | VO
Molecular Biotechnology	2	In silico Biology & AI | ILV
Molecular Biotechnology	2	Biologicals | VO
Molecular Biotechnology	2	Drug Screening & Development Methodologies | VO
Molecular Biotechnology	2	Drug Discovery Seminar | SE
Molecular Biotechnology	2	Molecular Immunology Seminar | SE
Molecular Biotechnology	2	RNA Seminar | SE
Molecular Biotechnology	2	Therapeutic Strategies | VO
Molecular Biotechnology	2	Vaccine Development | VO
Molecular Biotechnology	2	IP, Patent Law | VO
Molecular Biotechnology	2	Scientific Communication II | ILV
Molecular Biotechnology	2	Strategic Business Management | ILV
Molecular Biotechnology	2	Gene Therapy | VO
Molecular Biotechnology	2	Molecular Precision Medicine | VO
Molecular Biotechnology	2	Infection Biology | VO
Molecular Biotechnology	2	Molecular Virology | VO
Molecular Biotechnology	2	Molecular Pathology Lab | UE
Molecular Biotechnology	2	Signalling Pathways Lab | UE
Molecular Biotechnology	2	Stem Cells Lab | UE
Molecular Biotechnology	2	Signalling Pathways | VO
Molecular Biotechnology	2	Stem Cells & Organoids | VO
Molecular Biotechnology	3	Molecular Neurobiology | VO
Molecular Biotechnology	3	Tumour Biology | VO
Molecular Biotechnology	3	Vascular Biology | VO
Molecular Biotechnology	3	Drug Design & AI | VO
Molecular Biotechnology	3	Molecular Pharmacology | ILV
Molecular Biotechnology	3	Computerised Systems and Analytical Method Validation | ILV
Molecular Biotechnology	3	Pharmacovigilance & Regulatory Affairs | ILV
Molecular Biotechnology	3	Scientific Method: Drug Discovery | SE
Molecular Biotechnology	3	Scientific Method: Immunology | SE
Molecular Biotechnology	3	Allergies & Autoimmune Diseases | VO
Molecular Biotechnology	3	Clinical Aspects of Immunology | VO
Molecular Biotechnology	3	Computational Data Analysis | ILV
Molecular Biotechnology	3	Mass-spectrometry-based Omics Technologies | ILV
Molecular Biotechnology	3	Innovation in Biotechnology & Start-ups | ILV
Molecular Biotechnology	3	Master Project Seminar | SE
Molecular Biotechnology	3	Molecular Immunology Lab | UE
Molecular Biotechnology	3	Toxicology Lab | UE
Molecular Biotechnology	4	Master Exam | AP
Molekulare Biotechnologie	1	Biotechnologie & Wir | ILV
Molekulare Biotechnologie	1	Öffentliches Recht | VO
Molekulare Biotechnologie	1	Social Skills I: Präsentation & Auftritt | ILV
Molekulare Biotechnologie	1	Analytische Chemie | VO
Molekulare Biotechnologie	1	Analytische Chemie Labor | UE
Molekulare Biotechnologie	1	Allgemeine Chemie | VO
Molekulare Biotechnologie	1	Allgemeine Biologie | VO
Molekulare Biotechnologie	1	Allgemeine Zellbiologie | VO
Molekulare Biotechnologie	1	Grundlagen der Mikrobiologie | VO
Molekulare Biotechnologie	1	Mikroskopie Labor | UE
Molekulare Biotechnologie	1	Molekularbiologie der DNA | VO
Molekulare Biotechnologie	1	Chemisches Rechnen | ILV
Molekulare Biotechnologie	1	Mathematik in der Biologie I | ILV
Molekulare Biotechnologie	2	Ethics in Biotechnology | ILV
Molekulare Biotechnologie	2	Social Skills II: Selbstcoaching & Kommunikation | ILV
Molekulare Biotechnologie	2	Biochemie I: Grundlagen & Bausteine des Lebens | VO
Molekulare Biotechnologie	2	Organische Chemie | VO
Molekulare Biotechnologie	2	Quantitative Analytische Chemie | VO
Molekulare Biotechnologie	2	Quantitative Analytische Chemie Labor | UE
Molekulare Biotechnologie	2	Genexpression | VO
Molekulare Biotechnologie	2	Rekombinante DNA | VO
Molekulare Biotechnologie	2	Mathematik in der Biologie II | ILV
Molekulare Biotechnologie	2	Statistik in der Biologie I | ILV
Molekulare Biotechnologie	2	Angewandte Mikrobiologie | VO
Molekulare Biotechnologie	2	Virologie | VO
Molekulare Biotechnologie	2	Zellbiologie der Eukaryoten | VO
Molekulare Biotechnologie	3	Qualitäts- und Prozessmanagement | VO
Molekulare Biotechnologie	3	Bioinformatik | ILV
Molekulare Biotechnologie	3	Statistik in der Biologie II | ILV
Molekulare Biotechnologie	3	Biochemie II: Strukturbildung, Bioerkennung und Katalyse | VO
Molekulare Biotechnologie	3	Physikalische Chemie | VO
Molekulare Biotechnologie	3	Immunologie | VO
Molekulare Biotechnologie	3	Zellkultur | VO
Molekulare Biotechnologie	3	Einführung in das Molekularbiologische Arbeiten Labor | UE
Molekulare Biotechnologie	3	Genetic Engineering Laboratory | UE
Molekulare Biotechnologie	3	Mikrobiologische Arbeitsmethoden Labor | UE
Molekulare Biotechnologie	3	Molekularbiologische & Biophysikalische Methoden | ILV
Molekulare Biotechnologie	3	English in Science & Career I | ILV
Molekulare Biotechnologie	3	Social Skills III: Teambuilding & Konfliktregelung | ILV
Molekulare Biotechnologie	4	GxP | ILV
Molekulare Biotechnologie	4	Projektmanagement | ILV
Molekulare Biotechnologie	4	Social Skills IV: Moderation & Problemlösung | ILV
Molekulare Biotechnologie	4	Genregulation | VO
Molekulare Biotechnologie	4	Molekulare Genetik | VO
Molekulare Biotechnologie	4	Proteinexpression & -reinigung Labor | UE
Molekulare Biotechnologie	4	Biochemie III: Bioenergetik und Metabolismus | VO
Molekulare Biotechnologie	4	Instrumentelle Analytik | VO
Molekulare Biotechnologie	4	KI in der Biotechnologie | ILV
Molekulare Biotechnologie	4	Protein- & Enzym-Biochemie Labor | UE
Molekulare Biotechnologie	4	English in Science & Career II | ILV
Molekulare Biotechnologie	4	Organoid Technologies | VO
Molekulare Biotechnologie	4	Zellkultur Labor | UE
Molekulare Biotechnologie	5	Berufspraktikum
Molekulare Biotechnologie	5	Bachelorarbeit & Wissenschaftliches Arbeiten | SE
Molekulare Biotechnologie	5	Berufspraktikum | PR
Molekulare Biotechnologie	6	Business Law (Privat-, Arbeits- und Unternehmensrecht) | VO
Molekulare Biotechnologie	6	BWL | VO
Molekulare Biotechnologie	6	Entrepreneurship | ILV
Molekulare Biotechnologie	6	Marketing | ILV
Molekulare Biotechnologie	6	Product Development & Life Cycle Management | ILV
Molekulare Biotechnologie	6	Histologie | VO
Molekulare Biotechnologie	6	Humanphysiologie | VO
Molekulare Biotechnologie	6	Modellorganismen | VO
Molekulare Biotechnologie	6	Tissue Engineering | VO
Molekulare Biotechnologie	6	Entwicklungsbiologie und Krebsentstehung | VO
Molekulare Biotechnologie	6	From Bench to Bedside: Insights into Pharma Research | VO
Molekulare Biotechnologie	6	Pharma: Idee zum Markt | ILV
Molekulare Biotechnologie	6	Robotic Lab Automation | ILV
Molekulare Biotechnologie	6	Bachelorprüfung | AP
Molekulare Biotechnologie	6	Berufspraktikumsreflexion | ILV
Molekulare Biotechnologie	6	Biotech - Karrieren | ILV
Multilingual Technologies	1	Introduction to Computational Linguistics | ILV
Multilingual Technologies	1	Introduction to Machine Learning for Language Processing | ILV
Multilingual Technologies	1	Statistical Methods for Language Processing | ILV
Multilingual Technologies	1	Multilingual and Crosslingual Methods and Language Resources | VO
Multilingual Technologies	1	Translation Technologies | VO
Multilingual Technologies	1	Programming and Algorithms for Language Technologies | VO
Multilingual Technologies	1	Programming and Algorithms for Language Technologies | UE
Multilingual Technologies	2	Machine Learning Methods for Language Processing | VO
Multilingual Technologies	2	Machine Learning Methods for Language Processing | UE
Multilingual Technologies	2	Information Design for Language Data | ILV
Multilingual Technologies	2	Information Extraction and Retrieval for Multilingual Natural Language Data | ILV
Multilingual Technologies	2	Speech Technologies | ILV
Multilingual Technologies	2	Basics in Machine Translation | ILV
Multilingual Technologies	2	Transcultural Communication | VO
Multilingual Technologies	3	Human-Computer Interaction for Computational Linguists | ILV
Multilingual Technologies	3	Software Engineering for Language Technologies | ILV
Multilingual Technologies	3	Advanced Machine Translation | ILV
Multilingual Technologies	3	Academic Writing | ILV
Multilingual Technologies	3	Internship FH Campus Wien | PR
Multilingual Technologies	3	Berufspraktikum
Multilingual Technologies	3	Internship Universität Wien | PR
Multilingual Technologies	4	Data Protection and Privacy for Computational Linguists | ILV
Multilingual Technologies	4	IT Project Management for Computational Linguists | ILV
Multilingual Technologies	4	Master's Finals - FH Campus Wien | AP
Multilingual Technologies	4	Master's Finals - Masterthesis Universität Wien | AP
Multilingual Technologies	4	Master's Finals - Universität Wien | AP
Multilingual Technologies	4	Master Colloquium | SE
Nachhaltige Verpackungstechnologie	1	Grundlagen der Betriebswirtschaft | ILV
Nachhaltige Verpackungstechnologie	1	Kostenrechnung und Controlling | ILV
Nachhaltige Verpackungstechnologie	1	Chemie in der Verpackungstechnik | ILV
Nachhaltige Verpackungstechnologie	1	Chemische Laborübungen | UE
Nachhaltige Verpackungstechnologie	1	Grundlagen der Nachhaltigkeit | ILV
Nachhaltige Verpackungstechnologie	1	Grundlagen der Verpackungstechnik | ILV
Nachhaltige Verpackungstechnologie	1	Grundlagen der Packstoffe und deren Herstellung | ILV
Nachhaltige Verpackungstechnologie	2	Einführung in die Digitalisierung | ILV
Nachhaltige Verpackungstechnologie	2	Anforderungen an Verpackungen aus Sicht des Füllguts | ILV
Nachhaltige Verpackungstechnologie	2	Wechselwirkung Packstoff-Füllgut | ILV
Nachhaltige Verpackungstechnologie	2	Einführung Life Cycle Assessment | ILV
Nachhaltige Verpackungstechnologie	2	Konsum- und Industriegütermarketing | ILV
Nachhaltige Verpackungstechnologie	2	Team und Führung | SE
Nachhaltige Verpackungstechnologie	2	Mikrobiologie und Hygiene | ILV
Nachhaltige Verpackungstechnologie	2	Physik in der Verpackungstechnik | ILV
Nachhaltige Verpackungstechnologie	2	Qualitätsmanagement und Arbeitssicherheit | ILV
Nachhaltige Verpackungstechnologie	2	Grundlagen und Rahmenbedingen der Abfüllung | ILV
Nachhaltige Verpackungstechnologie	2	Industrielle Praxis des Abfüllens und Verpackens | ILV
Nachhaltige Verpackungstechnologie	3	Drucktechnik | ILV
Nachhaltige Verpackungstechnologie	3	Kunststoff | ILV
Nachhaltige Verpackungstechnologie	3	Kunststoffverarbeitung | ILV
Nachhaltige Verpackungstechnologie	3	Einkauf und Supply Chain Management | ILV
Nachhaltige Verpackungstechnologie	3	Operational Controlling | ILV
Nachhaltige Verpackungstechnologie	3	Papier | ILV
Nachhaltige Verpackungstechnologie	3	Papierverarbeitung | ILV
Nachhaltige Verpackungstechnologie	4	Glas | ILV
Nachhaltige Verpackungstechnologie	4	Metall | ILV
Nachhaltige Verpackungstechnologie	4	Nachhaltigkeit und Verpackung | ILV
Nachhaltige Verpackungstechnologie	4	Projekt Nachhaltige Verpackung | ILV
Nachhaltige Verpackungstechnologie	4	Operational and Commercial Excellence | ILV
Nachhaltige Verpackungstechnologie	4	Spezielles Qualitätsmanagement | ILV
Nachhaltige Verpackungstechnologie	4	Verpackungsrecht | ILV
Nachhaltige Verpackungstechnologie	4	Recycling und Abfallwirtschaft | ILV
Nachhaltige Verpackungstechnologie	5	Computer Aided Design | UE
Nachhaltige Verpackungstechnologie	5	Verpackungsentwicklung und Design | ILV
Nachhaltige Verpackungstechnologie	5	Gesprächs- und Verhandlungstechnik | SE
Nachhaltige Verpackungstechnologie	5	Sales und Key Account Management | ILV
Nachhaltige Verpackungstechnologie	5	Logistik | ILV
Nachhaltige Verpackungstechnologie	5	Prüftechnik | ILV
Nachhaltige Verpackungstechnologie	5	Ecodesign | ILV
Nachhaltige Verpackungstechnologie	6	Bachelorarbeit | SE
Nachhaltige Verpackungstechnologie	6	Bachelorprüfung | AP
Nachhaltige Verpackungstechnologie	6	Bachelorarbeit
Nachhaltige Verpackungstechnologie	6	Berufspraktikum | PR
Nachhaltige Verpackungstechnologie	6	Praktikumsreflexion | SE
Nachhaltiges Ressourcenmanagement	1	Betriebs- und Arbeitsorganisation | ILV
Nachhaltiges Ressourcenmanagement	1	Grundlagen der Betriebswirtschaft | ILV
Nachhaltiges Ressourcenmanagement	1	Einführung in das nachhaltige Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	1	Ethik im Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	1	Methoden des Ressourcenmanagements | ILV
Nachhaltiges Ressourcenmanagement	1	Mathematik im Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	1	Naturwissenschaftliches Rechnen | UE
Nachhaltiges Ressourcenmanagement	1	Physik im Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	1	Einführung Umweltrecht | VO
Nachhaltiges Ressourcenmanagement	1	Rechtsgrundlagen | ILV
Nachhaltiges Ressourcenmanagement	1	Einführung wissenschaftliches Arbeiten | ILV
Nachhaltiges Ressourcenmanagement	2	Kostenrechnung und Controlling | ILV
Nachhaltiges Ressourcenmanagement	2	Chemie im Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	2	Ökologie und Umweltmikrobiologie | ILV
Nachhaltiges Ressourcenmanagement	2	Globale Sozial- ökologische Nachhaltigkeit | ILV
Nachhaltiges Ressourcenmanagement	2	Wirtschafts- und Umweltethik | ILV
Nachhaltiges Ressourcenmanagement	2	Ausgewählte Kapitel des Umweltrechts | ILV
Nachhaltiges Ressourcenmanagement	2	Stoffstrommanagement und Prozessoptimierung | ILV
Nachhaltiges Ressourcenmanagement	2	Werkstoffe des Ressourcenmanagements | ILV
Nachhaltiges Ressourcenmanagement	2	Qualitative und Quantitative Forschungsmethoden | ILV
Nachhaltiges Ressourcenmanagement	3	Abfall- und Kreislaufwirtschaft | ILV
Nachhaltiges Ressourcenmanagement	3	Datenmanagement und Statistik im nachhaltigen Ressourcenmanagement | ILV
Nachhaltiges Ressourcenmanagement	3	Digitalisierung | ILV
Nachhaltiges Ressourcenmanagement	3	Lebenszyklusanalyse | ILV
Nachhaltiges Ressourcenmanagement	3	Qualitätsmanagement | ILV
Nachhaltiges Ressourcenmanagement	3	Umweltverträglichkeitsprüfung | VO
Nachhaltiges Ressourcenmanagement	3	Seminar zum Stoffstrommanagement | SE
Nachhaltiges Ressourcenmanagement	3	Prozess- und Verfahrenstechnik | ILV
Nachhaltiges Ressourcenmanagement	4	Logistik | ILV
Nachhaltiges Ressourcenmanagement	4	Operational und Commercial Excellence | ILV
Nachhaltiges Ressourcenmanagement	4	Umwelttechnik | ILV
Nachhaltiges Ressourcenmanagement	4	Übungen zu Lebenszyklusanalyse | UE
Nachhaltiges Ressourcenmanagement	4	Projekt- und Prozessmanagement | ILV
Nachhaltiges Ressourcenmanagement	4	Umweltmanagementsysteme | ILV
Nachhaltiges Ressourcenmanagement	4	Einkauf und Supply-Chain-Management | ILV
Nachhaltiges Ressourcenmanagement	4	Ressourcenmanagement in Produktionswirtschaft und Handel | ILV
Nachhaltiges Ressourcenmanagement	4	Umwelt- und Ressourcenökonomie | ILV
Nachhaltiges Ressourcenmanagement	5	Research Design | ILV
Nachhaltiges Ressourcenmanagement	5	Ressource und Umwelt | ILV
Nachhaltiges Ressourcenmanagement	5	Energiewirtschaft und Energietechnologie | ILV
Nachhaltiges Ressourcenmanagement	5	Gesprächs- und Verhandlungstechnik | ILV
Nachhaltiges Ressourcenmanagement	5	Marketing und Produktmanagement | ILV
Nachhaltiges Ressourcenmanagement	5	Ecodesign | ILV
Nachhaltiges Ressourcenmanagement	5	Produktentwicklung | ILV
Nachhaltiges Ressourcenmanagement	5	ESG Management, CSRD und Reporting | ILV
Nachhaltiges Ressourcenmanagement	5	Betriebliche Abfallwirtschaft und Umweltmanagement | ILV
Nachhaltiges Ressourcenmanagement	5	Design Thinking | ILV
Nachhaltiges Ressourcenmanagement	5	Ethics | ILV
Nachhaltiges Ressourcenmanagement	5	Waste Prevention and Preservation of Resources | ILV
Nachhaltiges Ressourcenmanagement	6	Bachelorarbeit | SE
Nachhaltiges Ressourcenmanagement	6	Bachelorprüfung | AP
Nachhaltiges Ressourcenmanagement	6	Bachelorarbeit
Nachhaltiges Ressourcenmanagement	6	Berufspraktikum | PR
Nachhaltiges Ressourcenmanagement	6	Praktikumsreflexion | SE
Orthoptik	1	Allgemeine Anatomie | VO
Orthoptik	1	Allgemeine Physiologie | VO
Orthoptik	1	Grundlagen der Pharmakologie | VO
Orthoptik	1	Hygiene | VO
Orthoptik	1	Allgemeine Pathologie und Organpathologien | VO
Orthoptik	1	Neurologie | VO
Orthoptik	1	Einführung in Orthoptik, Pleoptik und Strabologie | VO
Orthoptik	1	Physiologisches und pathologisches Binokularsehen | VO
Orthoptik	1	Praktische Übungen: Physiologisches und pathologisches Binokularsehen | UE
Orthoptik	1	Anatomie des Auges und Neuroanatomie | VO
Orthoptik	1	Physiologie des Auges | VO
Orthoptik	1	Gerätekunde und orthoptische Methodik 1 | VO
Orthoptik	1	Praktische Übungen: Orthoptische Methodik 1 | UE
Orthoptik	1	Einführung Literaturrecherche und Zitieren | ILV
Orthoptik	1	Einführung zu Studium und Beruf | VO
Orthoptik	1	Lern- und Arbeitstechniken | ILV
Orthoptik	2	Amblyopie und Pleoptik | VO
Orthoptik	2	Konkomitantes Schielen | VO
Orthoptik	2	Nystagmus | VO
Orthoptik	2	Kinderophthalmologie | VO
Orthoptik	2	Ophthalmologie | VO
Orthoptik	2	Ophthalmologische Untersuchungsmethoden | ILV
Orthoptik	2	Augenoptik | ILV
Orthoptik	2	Gerätekunde und orthoptische Methodik 2 | VO
Orthoptik	2	Heterophorie und Asthenopie | VO
Orthoptik	2	Praktische Übungen: Orthoptische Methodik 2 | UE
Orthoptik	2	Studien-, Praxisbegleitung, Reflexion 1 | ILV
Orthoptik	2	Medizinisches Englisch 1 | ILV
Orthoptik	3	Inkomitantes Schielen | ILV
Orthoptik	3	Praktische Übungen: Inkomitantes Schielen | UE
Orthoptik	3	Neuroophthalmologie | VO
Orthoptik	3	Neuroorthoptik | ILV
Orthoptik	3	Praktische Übungen: Neuroorthoptik | UE
Orthoptik	3	Grundlagen der Kontaktlinsenanpassung | ILV
Orthoptik	3	Praktische Übungen: Refraktionsbestimmung | UE
Orthoptik	3	Refraktionsbestimmung | ILV
Orthoptik	3	Grundlagen der Psychologie und Soziologie | ILV
Orthoptik	3	Kinderheilkunde | VO
Orthoptik	3	Kinderpsychologie | VO
Orthoptik	3	Kommunikation und Gesprächsführung | ILV
Orthoptik	3	Pädagogik | ILV
Orthoptik	3	Studien-, Praxisbegleitung, Reflexion 2 | ILV
Orthoptik	3	Einführung in wissenschaftliches Arbeiten | ILV
Orthoptik	4	Berufskunde und -ethik | ILV
Orthoptik	4	Freie Lehrveranstaltung: Barrierefreie Kommunikation | ILV
Orthoptik	4	Orthoptische Fallanalysen 1 | ILV
Orthoptik	4	Studien-, Praxisbegleitung, Reflexion 3 | ILV
Orthoptik	4	Digital Health Literacy | ILV
Orthoptik	4	Prävention und Gesundheitsförderung, Arbeitsmedizin / Bildschirmarbeit | ILV
Orthoptik	4	Schieloperationen | ILV
Orthoptik	4	Spezialbereiche der Orthoptik | ILV
Orthoptik	4	Low Vision Rehabilitation und vergrößernde Sehhilfen | ILV
Orthoptik	4	Neurorehabilitation | VO
Orthoptik	4	Orthoptische Rehabilitation bei zentralen Sehstörungen | ILV
Orthoptik	4	Sehbehinderung und Förderung im Kindesalter | ILV
Orthoptik	4	Visuelle Wahrnehmungsstörungen | ILV
Orthoptik	4	Einführung in die Statistik | ILV
Orthoptik	4	Theorie, Praxis und Methoden wissenschaftlichen Arbeitens | ILV
Orthoptik	4	Einführung in Projektmanagement | ILV
Orthoptik	4	Schreibwerkstatt | SE
Orthoptik	5	Berufspraktikum
Orthoptik	5	Studien-, Praxisbegleitung, Reflexion 4 | ILV
Orthoptik	5	Bewältigung herausfordernder Situationen im interprofessionellen und internationalen Kontext (mit ET und LP) | SE
Orthoptik	5	Interdisziplinäre Zusammenarbeit | ILV
Orthoptik	5	Orthoptische Fallanalysen 2 | ILV
Orthoptik	6	Freiberuflichkeit und betriebswirtschaftliche Grundlagen | ILV
Orthoptik	6	Grundzüge des Gesundheitswesens und der Gesundheitsökonomie | VO
Orthoptik	6	Qualitätsmanagement | VO
Orthoptik	6	Rechtsgrundlagen für Gesundheitsberufe | VO
Orthoptik	6	Orthoptische Fallanalysen 3 | ILV
Orthoptik	6	Refraktionbestimmung 2 | ILV
Orthoptik	6	Studien-, Praxisbegleitung, Reflexion 5 | ILV
Orthoptik	6	Bachelorprüfung | AP
Orthoptik	6	Medizinisches Englisch 2 | ILV
Orthoptik	6	Seminar zur Bachelorarbeit | SE
Orthoptik	6	Wahlbereich: Kontaktlinsenanpassung – Vertiefung | ILV
Orthoptik	6	Wahlbereich: Low Vision Rehabilitation – Vertiefung | ILV
Orthoptik	6	Wahlbereich: Orthoptische Rehabilitation bei zentralen Sehstörungen – Vertiefung | ILV
Pflegepädagogik	1	Einführung in die Bildungswissenschaft und Bildungstheorien | ILV
Pflegepädagogik	1	Didaktische Modelle und Konzepte | ILV
Pflegepädagogik	1	Ethik und ethisches Handeln in der Pflegepraxis | ILV
Pflegepädagogik	1	Pflegeepidemiologie mit Schwerpunkt Chronische Krankheiten | ILV
Pflegepädagogik	1	Grundlagen der Kommunikation in der Lehre | ILV
Pflegepädagogik	1	Rhetorik und Präsentationstechnik | UE
Pflegepädagogik	1	Lehrenden Professionalisierung | ILV
Pflegepädagogik	1	Lehr- und Lernprozessgestaltung, einschl. Training 1 | ILV
Pflegepädagogik	1	Berufs- und hochschuldidaktische Konzepte in der Praxisanleitung | ILV
Pflegepädagogik	1	Gesprächsführung im Setting der Praxisanleitung | ILV
            """.trimIndent()
        )
        append('\n')
        append(
            """
Pflegepädagogik	1	Rahmenbedingungen, Anleitungsmodelle & Konzepte in der Praxisanleitung | ILV
Pflegepädagogik	1	Anleitungstraining in der Praxislernphase | PR
Pflegepädagogik	1	Feedback & Beurteilung in der Praxislernphase | SE
Pflegepädagogik	1	Instructor* Professionalisierung | ILV
Pflegepädagogik	1	Simulationsdesign von Szenarien | SE
Pflegepädagogik	1	Theoretische Grundlagen Simulationstraining | ILV
Pflegepädagogik	1	Simulationsbriefing und Debriefing | ILV
Pflegepädagogik	1	Simulationstraining | PR
Pflegepädagogik	2	Gesundheitsförderung und Prävention | SE
Pflegepädagogik	2	Diversitätssensible Pflegekonzepte | ILV
Pflegepädagogik	2	Einführung in die Pflegewissenschaft | ILV
Pflegepädagogik	2	Vertiefende Pflegeforschung | ILV
Pflegepädagogik	2	EBN und Literaturrecherche | ILV
Pflegepädagogik	2	Lehr- und Lernprozessgestaltung, einschl. Training 2 | ILV
Pflegepädagogik	2	Leistungsfeststellung und - beurteilung | ILV
Pflegepädagogik	2	Methodische Konzeption des Unterrichts 2 | SE
Pflegepädagogik	2	Mediendidaktik und E-Learning | ILV
Pflegepädagogik	2	Methodische Konzeption des Unterrichts 1 | SE
Pflegepädagogik	2	Rechtsgrundlagen im Berufs- und Schulrecht | ILV
Pflegepädagogik	2	Curricula- und Lehrplanentwicklung | ILV
Pflegepädagogik	2	Coaching, Beratung und Konfliktmanagement | SE
Pflegepädagogik	3	Projekt- und Prozessmanagement | SE
Pflegepädagogik	3	Wissens- und Qualitätsmanagement | SE
Pflegepädagogik	3	Begleitseminar Abschlussarbeit | SE
Pflegepädagogik	3	Abschlussarbeit | SE
Pflegepädagogik	3	Abschlussprüfung | AP
Pflegepädagogik	3	Lehrpraxis | PR
Pflegepädagogik	3	Reflexion | PR
Physiotherapie	1	Anatomie in vivo | ILV
Physiotherapie	1	Anatomische Übungen | UE
Physiotherapie	1	Kompetenzentwicklung 1 | ILV
Physiotherapie	1	Phänomen Schmerz | ILV
Physiotherapie	1	Physiotherapeutischer Prozess im Berufsfeld | ILV
Physiotherapie	1	Public Health und Gesundheitsförderung | ILV
Physiotherapie	1	Entspannungstechniken | ILV
Physiotherapie	1	Klassische Massage | ILV
Physiotherapie	1	Motorisches Lernen und Methodik | ILV
Physiotherapie	1	Wahrnehmungs- und Haltungsschulung 1 | ILV
Physiotherapie	1	Haltungs- und Bewegungsanalyse | ILV
Physiotherapie	1	Mobilisation und professionelles Handling | ILV
Physiotherapie	1	Allgemeine Pathologie und Hygiene | VO
Physiotherapie	1	Anatomie 1 | VO
Physiotherapie	1	Physiologie 1 | VO
Physiotherapie	2	Clinical Reasoning | SE
Physiotherapie	2	Vorbereitung klinischer Praxis | UE
Physiotherapie	2	Angewandte Bewegungswissenschaften | ILV
Physiotherapie	2	Medizinische Trainingstherapie | ILV
Physiotherapie	2	Organsystem Klinik | VO
Physiotherapie	2	Fazilitation | ILV
Physiotherapie	2	Ganganalyse und Gangschulung | ILV
Physiotherapie	2	Physikalische Therapie | ILV
Physiotherapie	2	Sensomotorische Entwicklung | ILV
Physiotherapie	2	Strukturelle Untersuchung | ILV
Physiotherapie	2	Therapeutische Techniken und Übungen | ILV
Physiotherapie	2	Wahrnehmungs- und Haltungsschulung 2 | ILV
Physiotherapie	2	Anatomie 2 | VO
Physiotherapie	2	Physiologie 2 | VO
Physiotherapie	3	Neurologie 1 | ILV
Physiotherapie	3	Neurophysiologische Konzepte | ILV
Physiotherapie	3	Pädiatrie 1 | ILV
Physiotherapie	3	Funktionelle Verbände | ILV
Physiotherapie	3	Manualtherapie 1 | ILV
Physiotherapie	3	Orthopädie/Traumatologie und Bildgebung | VO
Physiotherapie	3	Orthopädie/Traumatologie und Rheumatologie 1 | ILV
Physiotherapie	3	Gynäkologie/Geburtshilfe/Urologie 1 | ILV
Physiotherapie	3	Innere Medizin und Chirurgie 1 | ILV
Physiotherapie	3	Lymphologische Physiotherapie | ILV
Physiotherapie	3	Respiratorische Physiotherapie | ILV
Physiotherapie	3	Literature Seminar | SE
Physiotherapie	3	Wissenschaftliches Arbeiten und Statistik | ILV
Physiotherapie	4	Neurologie 2 | ILV
Physiotherapie	4	Pädiatrie 2 | ILV
Physiotherapie	4	Manualtherapie 2 | ILV
Physiotherapie	4	Orthopädie/Traumatologie und Rheumatologie 2 | ILV
Physiotherapie	4	Professionelle Gesprächsführung und Interaktion 1 | ILV
Physiotherapie	4	Innere Medizin und Chirurgie und Gynäkologie 2 | ILV
Physiotherapie	4	Geriatrie und Gerontopsychiatrie | ILV
Physiotherapie	4	Intensivmedizin | ILV
Physiotherapie	4	Onkologie und Palliative Care/Hospizwesen | SE
Physiotherapie	4	Psychiatrie und Psychosomatik | ILV
Physiotherapie	4	Schreibwerkstatt | UE
Physiotherapie	5	Bachelorarbeitsseminar 1 | SE
Physiotherapie	5	Fachsupervision 1 | SE
Physiotherapie	5	Fachsupervision 2 | SE
Physiotherapie	5	Klinische Prüfung 1 | SE
Physiotherapie	5	Beruf, Recht und Wirtschaft | VO
Physiotherapie	5	Ethik und Gesellschaft | ILV
Physiotherapie	5	Professionelle Gesprächsführung und Interaktion 2 | ILV
Physiotherapie	6	Bachelorarbeitsseminar 2 | SE
Physiotherapie	6	Bachelorprüfung | UE
Physiotherapie	6	Klinische Prüfung 2 | SE
Physiotherapie	6	Kompetenzentwicklung 2 | SE
Physiotherapie	6	Bezugswissenschaften | SE
Physiotherapie	6	Practical Add Ons | SE
Physiotherapie	6	Reflexzonen- und Meridiantherapien | ILV
Primary Health Care Nursing	1	Assessment, Diagnostischer Prozess und Therapie chronischer Wunden | ILV
Primary Health Care Nursing	1	Management von Komorbiditäten bei chronischen Wunden | ILV
Primary Health Care Nursing	1	Spezielle Labordiagnostik und Medikamentenmanagement | ILV
Primary Health Care Nursing	1	Advanced Clinical Assessment & Pflegebedarfserhebung | ILV
Primary Health Care Nursing	1	Integrierte Versorgung im Lebensverlauf | ILV
Primary Health Care Nursing	1	Versorgungsformen bei Akutfällen | ILV
Primary Health Care Nursing	1	Versorgungsformen bei chronisch Kranken und komplexen Fällen | ILV
Primary Health Care Nursing	1	Ausgewählte Beratungs- und Schulungsmethoden | ILV
Primary Health Care Nursing	1	Beratung von Individuen, Gruppen und Organisationen | ILV
Primary Health Care Nursing	1	Qualitative Forschungsmethoden | ILV
Primary Health Care Nursing	1	Wissenschaftliches Arbeiten | ILV
Primary Health Care Nursing	1	Quantitative Forschungsmethoden | ILV
Primary Health Care Nursing	1	Statistik | ILV
Primary Health Care Nursing	2	Qualitäts- und Risikomanagement | ILV
Primary Health Care Nursing	2	Nurse Care Coordinator | ILV
Primary Health Care Nursing	2	Rechtsgrundlagen in der Primärversorgung | ILV
Primary Health Care Nursing	2	Kommissionelle Abschlussprüfung | AP
Primary Health Care Nursing	2	Abschlussarbeit in Verbindung mit integrierter Versorgung | SE
Primary Health Care Nursing	2	Betreuungsangebote und Finanzierungssysteme im Sozial- und Gesundheitswesen | ILV
Primary Health Care Nursing	2	Digitalisierung im Gesundheitssystem | ILV
Primary Health Care Nursing	2	Interprofessionelles Arbeiten | ILV
Primary Health Care Nursing	2	Elemente der Gesundheitskommunikation | ILV
Primary Health Care Nursing	2	eCounseling und eHealth | ILV
Primary Health Care Nursing	2	Inhaltsorientierte Gesundheits- und Pflegeberatung | ILV
Primary Health Care Nursing	2	Fallorientierte Gesundheits- und Pflegeberatung | ILV
Primary Health Care Nursing	2	Epidemiologie und Demographie | ILV
Primary Health Care Nursing	2	Evaluation, Health Impact & Health Technology Assessment (HIA, HTA) | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Psychiatrie und Gesellschaft | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Konzepte zur Entstehung psychischer Erkrankung | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Begegnung und Begleitung von Menschen mit psychischen Erkrankungen, einschließlich kultur-sensibler Modelle | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Personenzentrierte Modelle | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Wissenschaftliches Arbeiten | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Quantitative Methoden und Statistik | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Qualitative Methoden | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Medizin 1 | VO
Psychiatrische Gesundheits- und Krankenpflege	1	Psychologie, Pädagogik, Soziologie 1 | VO
Psychiatrische Gesundheits- und Krankenpflege	1	Berufsspezifische Rechtsgrundlagen | VO
Psychiatrische Gesundheits- und Krankenpflege	1	3. Lernort: Schwerpunkt Deeskalations- und Sicherheitsmanagement | PR
Psychiatrische Gesundheits- und Krankenpflege	1	3. Lernort: Supervision - Praxis der Selbstempathie | PR
Psychiatrische Gesundheits- und Krankenpflege	1	Praktikum 1 einschließlich Reflexion und Supervision | PR
Psychiatrische Gesundheits- und Krankenpflege	1	Mental Health Caring | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Der Mensch in einer psychosozialen Krise | ILV
Psychiatrische Gesundheits- und Krankenpflege	1	Zielgruppen- und settingspezifische Gesprächstechniken | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Praktikum 2 einschließlich Reflexion und Supervision | PR
Psychiatrische Gesundheits- und Krankenpflege	2	Pflegeassessments und Pflegeprozess - settingspezifische Vertiefung | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Informatik und Social Media im Gesundheitswesen | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Betreuungssettings für Menschen mit psychischen Erkrankungen | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Kreativität und Soziotherapie | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Medizin 2 | VO
Psychiatrische Gesundheits- und Krankenpflege	2	Psychologie, Pädagogik, Soziologie 2 | VO
Psychiatrische Gesundheits- und Krankenpflege	2	Erwachsene Menschen mit psychischen Erkrankungen inkl. Sucht und Forensik | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Menschen mit Behinderungen | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Menschen mit neurologischen Erkrankungen | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Kinder und Jugendliche mit psychischen Erkrankungen | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Psychogeriatrische Pflege | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Menschen in der psychosozialen Rehabilitation einschließlich Psychosomatik (Wahlpflicht) | ILV
Psychiatrische Gesundheits- und Krankenpflege	2	Ambulante psychiatrische Versorgung (Wahlpflichtfach) | ILV
Psychiatrische Gesundheits- und Krankenpflege	3	Schriftliche Abschlussarbeit | SE
Psychiatrische Gesundheits- und Krankenpflege	3	Kommissionelle Abschlussprüfung | AP
Psychiatrische Gesundheits- und Krankenpflege	3	3. Lernort: Vertiefende Konzepte in der Neurologie | PR
Psychiatrische Gesundheits- und Krankenpflege	3	Praktikum 3 einschließlich Reflexion und Supervision | PR
Psychiatrische Gesundheits- und Krankenpflege	3	3. Lernort: Affektregulierung durch Mentalisierung | PR
Psychiatrische Gesundheits- und Krankenpflege	3	Praktikum 4 einschließlich Reflexion und Supervision | PR
Public Management	1	Einführung in das wissenschaftliche Denken | UE
Public Management	1	Selbstmanagement und Verantwortungskultur | UE
Public Management	1	Digitale Transformation | ILV
Public Management	1	Grundlagen der Sozioökonomie | ILV
Public Management	1	Grundlagen von Gemeinwohl und Public Management | ILV
Public Management	1	Verfassungs- und EU-Recht | ILV
Public Management	1	Organisationslehre und Organisationssteuerung | ILV
Public Management	1	Politisch-administrative Systeme von Österreich und Europäischer Union | VO
Public Management	1	Theorie von Staat und Verwaltung | VO
Public Management	1	Grundlagen der Volkswirtschaftslehre | VO
Public Management	1	Öffentliches Haushaltswesen | ILV
Public Management	2	Dienstrecht | ILV
Public Management	2	Berufspraktikum
Public Management	2	Berufspraktikum 1 | PR
Public Management	2	Grundlagen der öffentlichen Betriebswirtschaftslehre | VO
Public Management	2	Berufliches Schreiben | ILV
Public Management	2	Grundlagen des wissenschaftlichen Arbeitens | UE
Public Management	2	Kreativität und Reflexionstechniken | UE
Public Management	2	E-Government und E-Governance | ILV
Public Management	2	Rechtliche Aspekte der Digitalisierung und Blockchain | UE
Public Management	2	Theorien und Konzepte von Public Value | UE
Public Management	2	Allgemeines Verwaltungsverfahrensrecht | ILV
Public Management	2	Qualitätsmanagement | ILV
Public Management	2	Politikfeldanalyse und Public Governance | ILV
Public Management	3	Data Science, AI, Ethik und Digitaler Humanismus | ILV
Public Management	3	Arbeitsrecht | ILV
Public Management	3	Privatrecht | ILV
Public Management	3	Grundlagen in Rechnungswesen, Buchhaltung und Budgetierung | VO
Public Management	3	Kosten- und Leistungsrechnung | ILV
Public Management	3	Wissenschaftliche Methoden 1 | ILV
Public Management	3	Communicating in English in contemporary public administrations 1 | UE
Public Management	3	Gender Mainstreaming und Diversity Management | UE
Public Management	3	Gruppendynamik und Konfliktmanagement | UE
Public Management	3	Einführung in die Wirkungsorientierung, -steuerung und -kontrolle | ILV
Public Management	3	Statistik und quantitative Methodenlehre | ILV
Public Management	3	Statistische Datenanalyse und Business Analytics | ILV
Public Management	3	Klima- und Energiepolitik | ILV
Public Management	3	Österreich in aktuellen Nachhaltigkeitsentwicklungen | ILV
Public Management	3	Rechtliche und organisatorische Aspekte der Partizipation | ILV
Public Management	3	Theorie von Partizipation und von Demokratieprozessen | SE
Public Management	3	Gebietskörperschaften im 21. Jahrhundert: Strukturen, Herausforderungen und Entwicklungen | ILV
Public Management	3	Politik und Verwaltung in der Stadt: Strategien, Zeithorizonte und Entscheidungsprozesse | ILV
Public Management	3	Grundlagen des Wissensmanagements | UE
Public Management	3	Wissen in der öffentlichen Organisation: Theoretische Einführung | ILV
Public Management	4	Data Governance und Datenmanagement | SE
Public Management	4	Öffentliches Wirtschafts- und Gesellschaftsrecht | UE
Public Management	4	Investitionen, externe Effekte und Finanzierung | ILV
Public Management	4	Statistik | UE
Public Management	4	Wissenschaftliche Methoden 2 | ILV
Public Management	4	Communicating in English in contemporary public administrations 2 | UE
Public Management	4	Projektmanagement | ILV
Public Management	4	Prozessmanagement | UE
Public Management	4	Team und Individuum | ILV
Public Management	4	Teamentwicklung | UE
Public Management	4	Zielformulierung, Kennzahlenentwicklung und Monitoring in der öffentlichen Verwaltung | ILV
Public Management	4	Big Data, Science und Datenbankmanagment | UE
Public Management	4	Entscheidungsoptimierung und Prescriptive Analytics | UE
Public Management	4	Nachhaltigkeit in Organisationen der Verwaltung | UE
Public Management	4	Wirtschaftliche Transformation zur Nachhaltigkeit | UE
Public Management	4	Analyse und Bewertung von Partizipationsprozessen | UE
Public Management	4	Konfliktlösung und Mediation in Partizipationsprozessen | UE
Public Management	4	Ressourcensystem Stadt | UE
Public Management	4	Wissensmanagement in der Anwendung: Organisation | UE
Public Management	4	Wissensmanagement in der Anwendung: Team | UE
Public Management	5	Förderungen | ILV
Public Management	5	Berufspraktikum
Public Management	5	Übung zur Berufspraxis 1 | UE
Public Management	5	Evaluationstheorie, Evaluationssysteme und Monitoring | ILV
Public Management	5	Übung zur Bachelorarbeit 1 | UE
Public Management	5	Policy making in the EU | UE
Public Management	5	Public administration in EU and in international contexts | UE
Public Management	5	Bilanzierung und Bilanzanalyse | UE
Public Management	5	Operatives Controlling und Budgetierung | UE
Public Management	5	Ethik und Compliance | ILV
Public Management	5	Öffentlichkeitsarbeit und Reputationsmanagement | ILV
Public Management	5	Innovationslabor Public Governance 1 | ILV
Public Management	5	Wirkungsorientierung in der Anwendung | ILV
Public Management	6	Nachhaltiges Beschaffungsmanagement | ILV
Public Management	6	Vergaben | ILV
Public Management	6	Bachelorprüfung
Public Management	6	Bachelorprüfung | AP
Public Management	6	Berufspraktikum
Public Management	6	Berufspraktikum 2 | PR
Public Management	6	Übung zur Berufspraxis 2 | UE
Public Management	6	Übung zur Bachelorarbeit 2 | UE
Public Management	6	Intercultural management | ILV
Public Management	6	Presenting in English | UE
Public Management	6	Personalmanagement und Personalverwaltung | UE
Public Management	6	Verhandlungsführung | UE
Public Management	6	Service Design und Kund*innenprozesse | ILV
Public Management	6	Innovationslabor Public Governance 2 | UE
Radiologietechnologie	1	Allgemeine Pathologie und Hygiene | VO
Radiologietechnologie	1	Anatomie | VO
Radiologietechnologie	1	Grundlagen der Klinischen Chemie und Pharmakologie | VO
Radiologietechnologie	1	Physiologie | VO
Radiologietechnologie	1	Bilddatenentstehung und Datengenerierung | VO
Radiologietechnologie	1	Grundlagen der Bilddatenverarbeitung und Rekonstruktion und Datenmanagement | ILV
Radiologietechnologie	1	Medizinphysik | ILV
Radiologietechnologie	1	Technische Qualitätskontrolle | ILV
Radiologietechnologie	1	Angewandte Untersuchungen und Interventionen in der Projektionsradiographie | UE
Radiologietechnologie	1	Gerätetechnik | ILV
Radiologietechnologie	1	Untersuchungen und Interventionen in der Projektionsradiographie | VO
Radiologietechnologie	2	Computertomographie | ILV
Radiologietechnologie	2	Magnetresonanztomographie | ILV
Radiologietechnologie	2	Spezielle Strahlenschutzausbildung diagnostische Anwendung | ILV
Radiologietechnologie	2	Strahlenbiologie | VO
Radiologietechnologie	2	Strahlenschutz Grundausbildung und Rechtliche Grundlagen | ILV
Radiologietechnologie	3	Grundlagen und Radiopharmazeutik | ILV
Radiologietechnologie	3	Technische und physikalische Grundlagen | ILV
Radiologietechnologie	3	Integrative Nuklearmedizin | ILV
Radiologietechnologie	3	Interprofessionelle klinische Fallbesprechungen | VO
Radiologietechnologie	3	Tumorlehre und Therapiekonzepte | VO
Radiologietechnologie	3	Patientensicherheit, Risikomanagement und Notfallmaßnahmen | ILV
Radiologietechnologie	3	Spezielle Strahlenschutzausbildung hinsichtlich der therapeutischen Anwendung | ILV
Radiologietechnologie	3	Spezielle Strahlenschutzausbildung offene radioaktive Stoffe und klinische Anwendungen | ILV
Radiologietechnologie	4	Angiographie, interventionelle Radiologie und kardiologische Angiographie | ILV
Radiologietechnologie	4	Projektmanagement | ILV
Radiologietechnologie	4	Qualitätsmanagement | ILV
Radiologietechnologie	4	Teletherapeutische Prozesse | ILV
Radiologietechnologie	4	Einführung in Radioonkologie und Gerätetechnik | ILV
Radiologietechnologie	4	Radioonkologische Spezialverfahren | ILV
Radiologietechnologie	4	Einführung in wissenschaftliches Arbeiten | ILV
Radiologietechnologie	4	Wissenschaftliche Methoden | ILV
Radiologietechnologie	5	Digitale Technologien in der Radiologietechnologie | ILV
Radiologietechnologie	5	Medizinische Datenvisualisierung und Analyse | UE
Radiologietechnologie	5	Datenauswertung | UE
Radiologietechnologie	5	Planung und Methodik | SE
Radiologietechnologie	5	Sonographie | ILV
Radiologietechnologie	6	Bachelorprojekt: Bachelorarbeit | SE
Radiologietechnologie	6	Bachelorprüfung | AP
Radiologietechnologie	6	Elective Course: Advanced Professional Skills 1 | ILV
Radiologietechnologie	6	Elective Course: Advanced Professional Skills 2 | ILV
Radiologietechnologie	6	Elective Course: Advanced Professional Skills 3 | ILV
Radiologietechnologie	6	Berufs- und Medizinethik | UE
Radiologietechnologie	6	Gesundheitsökonomie | ILV
Radiologietechnologie	6	Recht für Gesundheitsberufe | VO
Radiologietechnologie	6	Intercultural aspects in the professional field | ILV
Radiologietechnologie	6	Interprofessional aspects in the professional field | ILV
Radiologietechnologie	6	Patient-Centered Communication | ILV
Simulation in Health Care	1	Impro Workshop - Embracing Challenges | UE
Simulation in Health Care	1	Professionelle Integrität in der Simulation | ILV
Simulation in Health Care	1	Einführung in die psychologische Sicherheit | ILV
Simulation in Health Care	1	Lerntheoretische Grundlagen für simulationsbasiertes Lernen | ILV
Simulation in Health Care	1	Lernergebnisse formulieren | UE
Simulation in Health Care	1	Health Care Standards - Best Practice | ILV
Simulation in Health Care	1	Wissenschaftliches Arbeiten | ILV
Simulation in Health Care	1	Einführung in die Simulationsmodalitäten und das Konzept der Fidelity | ILV
Simulation in Health Care	1	Low Fidelity / Task-Based-Trainers (Skills-Training) | ILV
Simulation in Health Care	1	High Fidelity Simulation | ILV
Simulation in Health Care	1	Computerbasierte Simulation (z.B. VR-Simulation, Augmented Reality) | UE
Simulation in Health Care	1	Simulation mit Schauspielpatient*innen | UE
Simulation in Health Care	1	Formative und summative Assessments in der Simulation | ILV
Simulation in Health Care	2	Einführung in Simulationstechnologien | ILV
Simulation in Health Care	2	Gestaltung und Betrieb von Simulationszentren | ILV
Simulation in Health Care	2	Angewandtes Simulationsdesign | UE
Simulation in Health Care	2	Kernkonzepte für IPE/ IPC | ILV
Simulation in Health Care	2	Rollen der Teammitglieder in der interprofessionellen Zusammenarbeit | ILV
Simulation in Health Care	2	Interprofessionelle Teamarbeit und Kommunikation | UE
Simulation in Health Care	2	Wesentliche Faktoren für Szenarienentwicklung bei interprofessioneller Ausbildung und Zusammenarbeit | ILV
Simulation in Health Care	2	Human Factors und Crew/Crisis Resource Management | ILV
Simulation in Health Care	2	Kommunikationspsychologischen Grundlagen | ILV
Simulation in Health Care	2	Gute Gesprächsqualität im Gesundheitswesen | UE
Simulation in Health Care	2	Prebriefing, Briefing und Frameworks für Debriefing | ILV
Simulation in Health Care	2	Prebriefing und Debriefing Workshop 1 | UE
Simulation in Health Care	2	Supervision und Intervision 1 | UE
Simulation in Health Care	3	Umgang mit anspruchsvollen Debriefingsituationen (Reaktanz etc.) | ILV
Simulation in Health Care	3	Debriefingworkshop 2 | UE
Simulation in Health Care	3	Supervision und Intervision 2 | UE
Simulation in Health Care	3	Szenarienentwicklung | ILV
Simulation in Health Care	3	Praxis Szenarienentwicklung | UE
Simulation in Health Care	3	Ethik, Diversität und Chancengleichheit in Simulation | ILV
Simulation in Health Care	3	Forschungsethik und Ethikantrag für die Masterarbeit | ILV
Simulation in Health Care	3	Computational Thinking & Human Computer Interaction | ILV
Simulation in Health Care	3	AI in Simulation 1 | ILV
Simulation in Health Care	3	Projektplanerstellung – Von der Forschungsfrage zum Exposé | ILV
Simulation in Health Care	3	BASICS Qualitative und Quantitative Forschung und Statistik | ILV
Simulation in Health Care	3	Statistik 1 - Einführung und Übung | UE
Simulation in Health Care	4	AI in Simulation 2 | ILV
Simulation in Health Care	4	Extended Reality Simulations | ILV
Simulation in Health Care	4	Journal Club: Aktuelle Entwicklungen in der Simulation | UE
Simulation in Health Care	4	Wahlfach: Statistik 2 - komplexe quantitative Auswertungsverfahren | UE
Simulation in Health Care	4	Wahlfach: Qualitative Vertiefung - Erhebung und Auswertung qualitativer Daten | UE
Simulation in Health Care	4	Masterthesis Begleitseminar | SE
Simulation in Health Care	4	Masterthesis Examination | AP
Software Design and Engineering	1	Dependable and Scalable Infrastructures | ILV
Software Design and Engineering	1	Advanced Project Management | ILV
Software Design and Engineering	1	Cloud Computing | ILV
Software Design and Engineering	1	Requirements Engineering | ILV
Software Design and Engineering	1	Software Architectures | VO
Software Design and Engineering	1	Advanced Software Development | ILV
Software Design and Engineering	1	Software Engineering Project 1 | UE
Software Design and Engineering	2	Service Engineering | ILV
Software Design and Engineering	2	DevOps | ILV
Software Design and Engineering	2	Software Integration | ILV
Software Design and Engineering	2	Software Engineering Project 2 | UE
Software Design and Engineering	2	AI Engineering | ILV
Software Design and Engineering	2	Complex Problem Solving | ILV
Software Design and Engineering	2	Software Testing | ILV
Software Design and Engineering	3	Master Thesis Project | UE
Software Design and Engineering	3	Deep Learning | ILV
Software Design and Engineering	3	Digital Leadership | ILV
Software Design and Engineering	3	Distributed Ledger Technologies | ILV
Software Design and Engineering	3	Game Engineering | ILV
Software Design and Engineering	3	Advanced Embedded and IoT Security | ILV
Software Design and Engineering	3	Digital Forensics & Incident Response | ILV
Software Design and Engineering	3	Emerging Technologies and Future Threats | ILV
Software Design and Engineering	3	Penetration Testing | ILV
Software Design and Engineering	3	Mobile App Development | ILV
Software Design and Engineering	3	Secure Software Development | ILV
Software Design and Engineering	3	User Centered Design | ILV
Software Design and Engineering	3	Web Engineering | ILV
Software Design and Engineering	4	Entrepreneurship | VO
Software Design and Engineering	4	Legal IT Aspects | VO
Software Design and Engineering	4	Master Examination | AP
Software Design and Engineering	4	Master Thesis Seminar | SE
Sonography	1	Abdominal Pathologies, Clinical presentations and Reporting | ILV
Sonography	1	Abdominal Ultrasound Skills Lab, Reflection and Case Reports | UE
Sonography	1	Anatomy, Physiology and Clinical Applications of Abdominal Ultrasound | VO
Sonography	1	Basics of Innovation and State-of-the-Art Technologies | ILV
Sonography	1	Ultrasound Physics and TQM Skills Lab | UE
Sonography	1	Ultrasound Physics, Clinical US Techniques and TQM | VO
Sonography	1	Anatomy, Physiology and Clinical Applications of Obstetric Ultrasound | VO
Sonography	1	Obstetric Pathologies, Clinical Presentations and Reporting | ILV
Sonography	1	Obstetric Ultrasound Skills Lab, Reflection and Case Reports | UE
Sonography	1	Anatomy, Physiology and Clinical Applications of Vascular Ultrasound | VO
Sonography	1	Vascular Pathologies, Clinical Presentations and Reporting | ILV
Sonography	1	Vascular Ultrasound Skills Lab, Reflection and Case reports | UE
Sonography	2	Anatomy, Physiology and Clinical Applications Echocardiograpyh | VO
Sonography	2	Echocardiographic Pathologies, Clinical Presentations and Reporting | ILV
Sonography	2	Echocardiographic Ultrasound Skills Lab, Reflection and Case Reports | UE
Sonography	2	Anatomy, Physiology and Clinical Applications of upper & lower musculoskeletal Regions and Nerve Ultrasound | VO
Sonography	2	MSK & Nerve Pathologies, Clinical Presentations and Reporting | ILV
Sonography	2	MSK & Nerve Ultrasound Skills Lab, Reflection and Case Reports | UE
Sonography	2	Anatomy, Physiology, Clinical Applications of Thyroid Gland & Neck Ultrasound | VO
Sonography	2	Thyroid Gland & Neck Pathologies, Clinical Presentations and Reporting | ILV
Sonography	2	Thyroid Gland & Neck Ultrasound Skills Lab, Reflection and Case Reports | UE
Soziale Arbeit	1	Beratung | SE
Soziale Arbeit	1	Ethik und Menschenrechte | ILV
Soziale Arbeit	1	Gender und Diversität | SE
Soziale Arbeit	1	Geschichte der Sozialen Arbeit und professionelles Handeln im Überblick | VO
Soziale Arbeit	1	Individualhilfe | ILV
Soziale Arbeit	1	Materielle Sicherung | SE
Soziale Arbeit	1	Rechtliche Bezüge zur materiellen Sicherung | ILV
Soziale Arbeit	1	Sozialpolitische und -ökonomische Bezüge | SE
Soziale Arbeit	1	Orientierung und Berufsfelder | ILV
Soziale Arbeit	1	Praxisreflexion 1 | SE
Soziale Arbeit	1	Wissenschaftlich Schreiben und Recherchieren | ILV
Soziale Arbeit	2	Familienunterstützung und Fremdunterbringung | SE
Soziale Arbeit	2	Kinder- und Jugendhilfe | SE
Soziale Arbeit	2	Rechtliche Bezüge zu Kinderschutz und Familienarbeit | ILV
Soziale Arbeit	2	Gesundheitsaspekte des Kindes- und Jugendalters | ILV
Soziale Arbeit	2	Psychologische Aspekte des Kindes- und Jugendalters | ILV
Soziale Arbeit	2	Soziale Arbeit mit Kindern und Jugendlichen | ILV
Soziale Arbeit	2	Praxisreflexion 2 | SE
Soziale Arbeit	2	Proseminararbeit und Präsentation | SE
Soziale Arbeit	2	Konfliktarbeit, Mediation und Verhandlung | SE
Soziale Arbeit	2	Soziale Gruppenarbeit | SE
Soziale Arbeit	2	Soziale Ungleichheit und Sozialer Ausschluss | ILV
Soziale Arbeit	2	Theorien Sozialer Arbeit | ILV
Soziale Arbeit	3	Biografie und Bewältigung im Lebensalter | ILV
Soziale Arbeit	3	Globale Ungleichheiten und Entwicklungspolitik | ILV
Soziale Arbeit	3	Krisenintervention | SE
Soziale Arbeit	3	Gesundheit und Soziale Arbeit | VO
Soziale Arbeit	3	Gesundheitsaspekte des Erwachsenenalters | ILV
Soziale Arbeit	3	Rechtliche Bezüge zu Gesundheitsthemen | ILV
Soziale Arbeit	3	Forschungswerkstatt 1 | SE
Soziale Arbeit	3	Praxisreflexion 3 | SE
Soziale Arbeit	3	Sozialforschung im Überblick | ILV
Soziale Arbeit	3	Gewaltschutz | SE
Soziale Arbeit	3	Sexualität | SE
Soziale Arbeit	3	Sucht | SE
Soziale Arbeit	3	Berufsfeld und Methoden - Offene Jugendarbeit | SE
Soziale Arbeit	3	Berufsfeld und Methoden - Stadtteilarbeit | SE
Soziale Arbeit	3	TeamTeaching inkl. Peer Group | SE
Soziale Arbeit	3	Theorien - Soziale Arbeit im Sozialen Raum | ILV
Soziale Arbeit	3	Berufsfeld und Methoden - Psychiatrie | SE
Soziale Arbeit	3	Berufsfeld und Methoden - Strafjustiz | SE
Soziale Arbeit	3	Theorien - Soziale Arbeit im Zwangskontext | ILV
Soziale Arbeit	3	Berufsfeld und Methoden - Arbeit und Qualifizierung | SE
Soziale Arbeit	3	Berufsfeld und Methoden - Schulsozialarbeit | SE
Soziale Arbeit	3	Theorien - Soziale Arbeit und Bildung | ILV
Soziale Arbeit	4	Fremdenrecht | VO
Soziale Arbeit	4	Rassismuskritisches Arbeiten | SE
Soziale Arbeit	4	Soziale Arbeit und Asyl/Migration | SE
Soziale Arbeit	4	Demokratie und Partizipation | ILV
Soziale Arbeit	4	Organisation in der Sozialen Arbeit | ILV
Soziale Arbeit	4	Selbstorganisation und politisches Handeln | SE
Soziale Arbeit	4	Forschungspräsentation und Thesisplan | ILV
Soziale Arbeit	4	Forschungswerkstatt 2 | SE
Soziale Arbeit	4	Praxisreflexion 4 | SE
Soziale Arbeit	4	Berufsfeld und Methoden - Offene Jugendarbeit | SE
Soziale Arbeit	4	Berufsfeld und Methoden - Stadtteilarbeit | SE
Soziale Arbeit	4	TeamTeaching inkl. Peer Group | SE
Soziale Arbeit	4	Theorien - Soziale Arbeit im Sozialen Raum | ILV
Soziale Arbeit	4	Berufsfeld und Methoden - Psychiatrie | SE
Soziale Arbeit	4	Berufsfeld und Methoden - Strafjustiz | SE
Soziale Arbeit	4	Theorien - Soziale Arbeit im Zwangskontext | ILV
Soziale Arbeit	4	Berufsfeld und Methoden - Arbeit und Qualifizierung | SE
Soziale Arbeit	4	Berufsfeld und Methoden - Schulsozialarbeit | SE
Soziale Arbeit	4	Theorien - Soziale Arbeit und Bildung | ILV
Soziale Arbeit	5	BA Thesis Seminar 1 | SE
Soziale Arbeit	5	Praxisreflexion 5 | SE
Soziale Arbeit	5	Wahl Methoden/Projekt | SE
Soziale Arbeit	5	Wahl Theorie/Projekt | SE
Soziale Arbeit	6	Wahl ATSA 2 | SE
Soziale Arbeit	6	Wahl ATSA1 | SE
Soziale Arbeit	6	BA Thesis Seminar 2 | SE
Soziale Arbeit	6	Praxisreflexion 6 | SE
Soziale Arbeit	6	BA Fallseminar | SE
Soziale Arbeit	6	BA Prüfung | SE
Soziale Arbeit	6	Rechtliche Vertiefung | SE
Soziale Arbeit	6	Wahl Theorie/Methode/Projekt | SE
Sozialmanagement in der Elementarpädagogik	2	Ausgewählte Aspekte der Sozialpolitik und Volkswirtschaftslehre | ILV
Sozialmanagement in der Elementarpädagogik	2	Grundlagen der Betriebswirtschaft | VO
Sozialmanagement in der Elementarpädagogik	2	Psychologische Grundlagen der frühen Kindheit | ILV
Sozialmanagement in der Elementarpädagogik	2	Soziologie der frühen Kindheit und Familie | ILV
Sozialmanagement in der Elementarpädagogik	2	Ethische Grundlagen von Erziehung und Bildung | ILV
Sozialmanagement in der Elementarpädagogik	2	Geschichte der Elementarpädagogik | ILV
Sozialmanagement in der Elementarpädagogik	2	Förderung und Unterstützung von frühkindlichen Bildungsprozessen | ILV
Sozialmanagement in der Elementarpädagogik	2	Pädagogische Diagnostik und Konzeptbildung | ILV
Sozialmanagement in der Elementarpädagogik	2	Klassische frühpädagogische Handlungskonzepte | ILV
Sozialmanagement in der Elementarpädagogik	2	Neue pädagogische Handlungskonzepte | ILV
Sozialmanagement in der Elementarpädagogik	2	Perspektiven auf Bildungsprozesse von Erwachsenen | SE
Sozialmanagement in der Elementarpädagogik	2	Perspektiven auf frühkindliche Bildungsprozesse | ILV
Sozialmanagement in der Elementarpädagogik	3	Beratung- und Gesprächsführung mit Eltern und Familien | ILV
Sozialmanagement in der Elementarpädagogik	3	Rechtliche Bezüge der Elementarpädagogik | VO
Sozialmanagement in der Elementarpädagogik	3	Fachsprache Englisch | ILV
Sozialmanagement in der Elementarpädagogik	3	Grundlagen der empirischen Sozialforschung | VO
Sozialmanagement in der Elementarpädagogik	3	Grundlagen wissenschaftlichen Arbeitens | ILV
Sozialmanagement in der Elementarpädagogik	3	Lektüreseminar | SE
Sozialmanagement in der Elementarpädagogik	3	Grundlagen der Buchhaltung und Bilanzierung | ILV
Sozialmanagement in der Elementarpädagogik	3	Grundlagen der Kostenrechnung | ILV
Sozialmanagement in der Elementarpädagogik	3	Inklusive Elementarpädagogik | ILV
Sozialmanagement in der Elementarpädagogik	3	Theorien der Elementarpädagogik | ILV
Sozialmanagement in der Elementarpädagogik	3	Transitionsprozesse in der frühen Kindheit | ILV
Sozialmanagement in der Elementarpädagogik	4	Begleitseminar Forschungspraxis | SE
Sozialmanagement in der Elementarpädagogik	4	Qualitative Erhebungs- und Auswertungsmethoden | ILV
Sozialmanagement in der Elementarpädagogik	4	Quantitative Erhebungs- und Auswertungsmethoden | ILV
Sozialmanagement in der Elementarpädagogik	4	Controlling | ILV
Sozialmanagement in der Elementarpädagogik	4	Nachhaltige Finanzierung und Investition | ILV
Sozialmanagement in der Elementarpädagogik	4	Diversität - Umgang mit Vielfalt und Fremdsein | ILV
Sozialmanagement in der Elementarpädagogik	4	Lebenswelten von Kindern und ihren Familien | SE
Sozialmanagement in der Elementarpädagogik	4	Beratung und Gesprächsführung im Team | ILV
Sozialmanagement in der Elementarpädagogik	4	Personalmanagement | ILV
Sozialmanagement in der Elementarpädagogik	4	Selbstreflexivität und berufliche Identitätsbildung | SE
Sozialmanagement in der Elementarpädagogik	4	Teambuilding | ILV
Sozialmanagement in der Elementarpädagogik	5	Konzeptseminar zur Bachelorarbeit | SE
Sozialmanagement in der Elementarpädagogik	5	Grundlagen psychodynamischer Managementtheorien | ILV
Sozialmanagement in der Elementarpädagogik	5	Grundlagen sozioökonomischer Managementtheorien | ILV
Sozialmanagement in der Elementarpädagogik	5	Netzwerke und Kommunikation | ILV
Sozialmanagement in der Elementarpädagogik	5	Projektmanagement | ILV
Sozialmanagement in der Elementarpädagogik	5	Unterstützungssysteme für Familien | ILV
Sozialmanagement in der Elementarpädagogik	5	Elementarpädagogik international | ILV
Sozialmanagement in der Elementarpädagogik	5	Präsentieren von Forschungsergebnissen | ILV
Sozialmanagement in der Elementarpädagogik	5	Work Discussion als Methode der Fallarbeit und Fallanalyse | SE
Sozialmanagement in der Elementarpädagogik	6	Bachelorprüfung | AP
Sozialmanagement in der Elementarpädagogik	6	Seminar zur Bachelorarbeit | SE
Sozialmanagement in der Elementarpädagogik	6	Marketing, Sponsoring, Fundraising | ILV
Sozialmanagement in der Elementarpädagogik	6	Sozialraumorientierte Elementarpädagogik | ILV
Sozialmanagement in der Elementarpädagogik	6	Individuum - Gruppe - Organisation | SE
Sozialmanagement in der Elementarpädagogik	6	Qualitätsmanagement und Qualitätsentwicklung | ILV
Sozialmanagement in der Elementarpädagogik	6	Gender und sexuelle Bildung in der frühen Kindheit | ILV
Sozialmanagement in der Elementarpädagogik	6	Krisenintervention | ILV
Sozialmanagement in der Elementarpädagogik	6	Sprachentwicklung und Sprachförderung | ILV
Sozialraumorientierte und Klinische Soziale Arbeit	1	Ausgewählte Themen der Psychiatrie | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	Ausgewählte Themen des österreichischen Sozialrechts | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	Ethik der Sozialen Arbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	1	Transdisziplinäres Studieren | UE
Sozialraumorientierte und Klinische Soziale Arbeit	1	Angewandte biopsychosoziale Klinische Fallführung | UE
Sozialraumorientierte und Klinische Soziale Arbeit	1	Biopsychosoziale Phänomene und Behandlungsmodelle | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	"Einführung" Klinische Soziale Arbeit | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	Qualitative Forschungsmethodik | SE
Sozialraumorientierte und Klinische Soziale Arbeit	1	Quantitative Forschungsmethodik | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	Aufsuchende und niederschwellige Soziale Arbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	1	"Einführung" Sozialräumliche Soziale Arbeit | VO
Sozialraumorientierte und Klinische Soziale Arbeit	1	Soziale Ungleichheiten | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Wahlpflichtfach 1 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Angewandte klinisch-soziale Fallführung | UE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Klinisch-soziale Diagnostik und Interventionsplanung | VO
Sozialraumorientierte und Klinische Soziale Arbeit	2	Gutachtenerstellung | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Sozialpsychiatrische Störungsbilder und Risk-Assessment | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Sucht | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Angewandte multivariate Datenanalyse 1 | VO
Sozialraumorientierte und Klinische Soziale Arbeit	2	Forschungsdesign für Masterarbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Fortgeschrittene qualitative Forschungsmethoden 1 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Regulierung öffentlicher Räume | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Urbane Transformationen | VO
Sozialraumorientierte und Klinische Soziale Arbeit	2	Wohnungslosigkeit und Sozialraumarbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Offene Kinder- und Jugendarbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Konzepte sozialräumlicher Sozialer Arbeit 1 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	2	Fortgeschrittene quantitative Forschungsmethoden | VO
Sozialraumorientierte und Klinische Soziale Arbeit	2	Methoden der Sozialraumanalyse | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Wahlpflichtfach 2 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Interventionelle klinisch-soziale Fallführung | UE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Klinisch-soziale Interventionsformen | VO
Sozialraumorientierte und Klinische Soziale Arbeit	3	Masterarbeits - Erstellung 1 | UE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Evaluation und Wirkung | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Trauma und Resilienz | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Angewandte multivariate Datenanalyse 2 | VO
Sozialraumorientierte und Klinische Soziale Arbeit	3	Fortgeschrittene qualitative Forschungsmethoden 2 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Wissenschaftliches Schreiben | UE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Masterarbeit-Erstellung 1 | UE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Triangulative sozialräumliche Forschung | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Gemeinwesen und Stadtteilarbeit | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Stadtentwicklung und Raumwahrnehmung | SE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Entwicklung sozialräumlicher Praxen | UE
Sozialraumorientierte und Klinische Soziale Arbeit	3	Konzepte sozialräumlicher Sozialer Arbeit 2 | SE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Masterarbeit- Erstellung 2 | UE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Klinische Studien | VO
Sozialraumorientierte und Klinische Soziale Arbeit	4	Masterkolloquium | AP
Sozialraumorientierte und Klinische Soziale Arbeit	4	Masterarbeit
Sozialraumorientierte und Klinische Soziale Arbeit	4	Masterarbeit-Coaching | UE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Reflexion und Selbst | UE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Sozialtherapeutische Fallführung | UE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Sozialtherapie | VO
Sozialraumorientierte und Klinische Soziale Arbeit	4	Masterarbeit-Erstellung 2 | UE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Sozialraumarbeit in institutionellen Räumen | SE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Sozialstaat und Sozialpolitik | SE
Sozialraumorientierte und Klinische Soziale Arbeit	4	Stadtökonomik | VO
Sozialraumorientierte und Klinische Soziale Arbeit	4	Evaluation und Wirkung | SE
Sozialwirtschaft	1	Organisation | SE
Sozialwirtschaft	1	Qualitätsmanagement | ILV
Sozialwirtschaft	1	Wirkungsorientierung | SE
Sozialwirtschaft	1	Leadership und Selbstmanagement | SE
Sozialwirtschaft	1	Soziale Arbeit als Profession in der Sozialwirtschaft | ILV
Sozialwirtschaft	1	Forschungs- und Evaluierungsdesign | ILV
Sozialwirtschaft	1	Umgang mit Komplexität | ILV
Sozialwirtschaft	1	Rechnungswesen in der Sozialwirtschaft | ILV
Sozialwirtschaft	1	Zivil- und Unternehmensrecht | ILV
Sozialwirtschaft	1	Einführung in die Sozialwirtschaft | VO
Sozialwirtschaft	2	Spring School 1 | ILV
Sozialwirtschaft	2	Synthese-Workshop 1 | SE
Sozialwirtschaft	2	Sozialmarketing | ILV
Sozialwirtschaft	2	Personalmanagement | ILV
Sozialwirtschaft	2	Controlling | ILV
Sozialwirtschaft	2	Forschungsthemenpool | SE
Sozialwirtschaft	2	Konzept Masterarbeit | ILV
Sozialwirtschaft	2	Arbeitsrecht | ILV
Sozialwirtschaft	2	Inklusives Diversitätsmanagement | ILV
Sozialwirtschaft	2	Sozialberichterstattung und Sozialplanung | ILV
Sozialwirtschaft	2	Sozialpolitische Grundlagen der Sozialwirtschaft | ILV
Sozialwirtschaft	3	Angewandte Empirie 1 | ILV
Sozialwirtschaft	3	Öffentlichkeitsarbeit | SE
Sozialwirtschaft	3	Finanzierung in der Sozialwirtschaft | ILV
Sozialwirtschaft	3	EU-Förderungen | SE
Sozialwirtschaft	3	Intercultural Project in Social Economy | ILV
Sozialwirtschaft	3	Projektmanagement | SE
Sozialwirtschaft	3	Nachhaltigkeitsmanagement und Green Controlling | ILV
Sozialwirtschaft	3	Innovationsmanagement | SE
Sozialwirtschaft	4	Spring School 2 | ILV
Sozialwirtschaft	4	Synthese-Workshop 2 | SE
Sozialwirtschaft	4	Angewandte Empirie 2 | UE
Sozialwirtschaft	4	Masterprüfung | AP
Sozialwirtschaft	4	Strategische Unternehmensführung | SE
Sozialwirtschaft	4	Wahlpflichtfach: Digital Leadership | SE
Sozialwirtschaft	4	Wahlpflichtfach: Unternehmensgründung | SE
Sozialwirtschaft	4	Wahlpflichtfach: Wirkungsmessung | SE
Sustainability Assessment and Resource Management	1	Biological resources and bioeconomy | ILV
Sustainability Assessment and Resource Management	1	Ecological cycles in nature | ILV
Sustainability Assessment and Resource Management	1	Material flow management and mass flow analysis | ILV
Sustainability Assessment and Resource Management	1	Selected topics of life cycle assessment | ILV
Sustainability Assessment and Resource Management	1	Law and standards in resource management | ILV
Sustainability Assessment and Resource Management	1	Scientific working | ILV
Sustainability Assessment and Resource Management	1	Leadership | ILV
Sustainability Assessment and Resource Management	1	Social sustainability and global challenges | ILV
Sustainability Assessment and Resource Management	1	Green project management | ILV
Sustainability Assessment and Resource Management	2	Industrial plant engineering and approval | ILV
Sustainability Assessment and Resource Management	2	Biodiversity and nature conservation in resource management | ILV
Sustainability Assessment and Resource Management	2	Applied life cycle assessment | UE
Sustainability Assessment and Resource Management	2	Energy technology | ILV
Sustainability Assessment and Resource Management	2	Professional English | ILV
Sustainability Assessment and Resource Management	2	Quantitative and qualitative methods in economic and social research | ILV
Sustainability Assessment and Resource Management	2	Internationalization in sustainability | ILV
Sustainability Assessment and Resource Management	2	Social life cycle assessment und impact evaluation | ILV
Sustainability Assessment and Resource Management	2	Alternative economic concepts for sustainable transformation | ILV
Sustainability Assessment and Resource Management	2	Sustainable innovation strategies and entrepreneurship | ILV
Sustainability Assessment and Resource Management	3	Practical course in environmental technology | UE
Sustainability Assessment and Resource Management	3	Selected and current topics in environmental technology | ILV
Sustainability Assessment and Resource Management	3	Energy economics and energy transformation | ILV
Sustainability Assessment and Resource Management	3	Materials and circular economy | ILV
Sustainability Assessment and Resource Management	3	Green claims and sustainable communication | ILV
Sustainability Assessment and Resource Management	3	Regulations in corporate sustainability management | ILV
            """.trimIndent()
        )
        append('\n')
        append(
            """
Sustainability Assessment and Resource Management	3	Data management and statistics in sustainability management | ILV
Sustainability Assessment and Resource Management	3	Ethics | ILV
Sustainability Assessment and Resource Management	3	Waste Prevention and Preservation of Resources | ILV
Sustainability Assessment and Resource Management	4	Project study: Sustainable product and process design | UE
Sustainability Assessment and Resource Management	4	Conflict management and presentation techniques | ILV
Sustainability Assessment and Resource Management	4	Master Exam | AP
Sustainability Assessment and Resource Management	4	Design Thinking | ILV
Sustainability Assessment and Resource Management	4	Research Project Management - Implementation | ILV
Sustainable Packaging Design and Technology	1	Communication in Sustainable Packaging | ILV
Sustainable Packaging Design and Technology	1	Presentation Techniques | SE
Sustainable Packaging Design and Technology	1	Professional English I | ILV
Sustainable Packaging Design and Technology	1	Regulations in Packaging | ILV
Sustainable Packaging Design and Technology	1	Green Project Management | ILV
Sustainable Packaging Design and Technology	1	Leadership | ILV
Sustainable Packaging Design and Technology	1	Introduction to Packaging Testing | VO
Sustainable Packaging Design and Technology	1	Science in Packaging Technology and Design (Journal Club) | ILV
Sustainable Packaging Design and Technology	1	Scientific Working | ILV
Sustainable Packaging Design and Technology	1	Life Cycle Assessment & Material Flow Analysis | ILV
Sustainable Packaging Design and Technology	1	Industrial Packaging | ILV
Sustainable Packaging Design and Technology	1	Sustainable Packaging Design | ILV
Sustainable Packaging Design and Technology	2	Professional English II | ILV
Sustainable Packaging Design and Technology	2	Entrepreneurship & Innovation | ILV
Sustainable Packaging Design and Technology	2	Environmental Law and EMS | ILV
Sustainable Packaging Design and Technology	2	Packaging Performance and Testing | UE
Sustainable Packaging Design and Technology	2	Quantitative and qualitative methods in research | ILV
Sustainable Packaging Design and Technology	2	Research Design for Master Thesis | SE
Sustainable Packaging Design and Technology	2	Applied Life Cycle Assessment | ILV
Sustainable Packaging Design and Technology	2	Advanced Packaging Technology | ILV
Sustainable Packaging Design and Technology	3	Concept Development for Master Thesis | SE
Sustainable Packaging Design and Technology	3	Current Topics in Sustainable Design and Packaging | ILV
Sustainable Packaging Design and Technology	3	Packaging Recycling Technologies | ILV
Sustainable Packaging Design and Technology	3	Trends and Innovation in Packaging | ILV
Sustainable Packaging Design and Technology	3	Business and Financial Management | ILV
Sustainable Packaging Design and Technology	3	Statistics and Data Management in the Packaging Industry | ILV
Sustainable Packaging Design and Technology	3	Packaging Safety Assessment with in Vitro Bioassays | ILV
Sustainable Packaging Design and Technology	3	Recyclability of coated paper | ILV
Sustainable Packaging Design and Technology	3	Waste Prevention and Preservation of Resources in Packaging | ILV
Sustainable Packaging Design and Technology	3	Environmental Protection in Packaging | ILV
Sustainable Packaging Design and Technology	3	Waste Management and Circular Economy | ILV
Sustainable Packaging Design and Technology	3	Change Management & Development | SE
Sustainable Packaging Design and Technology	3	Packaging Materials in a Circular Economy | ILV
Sustainable Packaging Design and Technology	4	Master Seminar | SE
Sustainable Packaging Design and Technology	4	Master´s Exam | AP
Sustainable Packaging Design and Technology	4	Design Thinking | ILV
Sustainable Packaging Design and Technology	4	Ethics | ILV
Sustainable Packaging Design and Technology	4	Research Project Management - Implementation | ILV
Sustainable Packaging Design and Technology	4	Varying Topics in the Field of Packaging & Sustainability | ILV
Sustainable Packaging Design and Technology	4	Packaging Development and Design | ILV
Tax Consulting	1	Betriebswirtschaftliche Steuerlehre | ILV
Tax Consulting	1	Investitions- und Finanzierungsrechnung | ILV
Tax Consulting	1	Jahresabschlussanalyse, Kennzahlen und Kennzahlensysteme | ILV
Tax Consulting	1	Kosten- und Leistungsrechnung einschließlich kurzfristige Erfolgsrechnung | SE
Tax Consulting	1	Planungsrechnung inkl. Fortbestehensprognose | ILV
Tax Consulting	1	Unternehmensbewertung | ILV
Tax Consulting	1	Erstellung von Jahresabschlüssen, Sonderfragen des Jahresabschlusses und Inhalt des Lageberichts (UGB) | ILV
Tax Consulting	1	Grundzüge der Internationalen Rechnungslegung | ILV
Tax Consulting	1	Grundzüge der Personalverrechnung | ILV
Tax Consulting	1	Konzernrechnungslegung | ILV
Tax Consulting	2	Spezialfragen des Finanz- und Wirtschaftsstrafrechts | ILV
Tax Consulting	2	Spezialfragen des Verfahrensrechts | ILV
Tax Consulting	2	Spezialfragen des Einkommensteuerrechts I | VO
Tax Consulting	2	Spezialfragen des Körperschaftsteuerrechts | ILV
Tax Consulting	2	Unionsrecht | SE
Tax Consulting	2	Verfassungsrecht | SE
Tax Consulting	2	Unternehmens- und Gesellschaftsrecht | ILV
Tax Consulting	2	Aktuelle Entwicklungen der abgabenrechtlichen Rechtsprechung wie Ökologisierung des Steuersystems | VO
Tax Consulting	2	Spezialfragen des Umsatzsteuerrechts national | ILV
Tax Consulting	2	Forschungsmethodik | ILV
Tax Consulting	2	Schreiben im wissenschaftlichen Kontext | SE
Tax Consulting	3	Gemeinnützigkeitsrecht | VO
Tax Consulting	3	Spezialfragen des Einkommensteuerrechts II | SE
Tax Consulting	3	OECD-Richtlinien und Musterabkommen (inkl. DBA) | SE
Tax Consulting	3	OECD-Verrechnungspreise | SE
Tax Consulting	3	Österreichisches Außensteuerrecht und Rechtsvergleiche | SE
Tax Consulting	3	Masterarbeit
Tax Consulting	3	Themenfindung Masterarbeit – die Forschungsfrage | SE
Tax Consulting	3	Bürgerliches Recht | ILV
Tax Consulting	3	Insolvenzrecht | ILV
Tax Consulting	3	Umgründungssteuerrecht | ILV
Tax Consulting	3	Umsatzsteuerrecht im Binnenmarkt | ILV
Tax Consulting	3	Verkehrssteuern, ausgewählte sonstige Abgaben | VO
Tax Consulting	4	Führung, Kommunikation, Konfliktmanagement | ILV
Tax Consulting	4	Führungsstärke, Verhandlung und Beratung | ILV
Tax Consulting	4	Gelebte Beratung & Kanzleimanagement | ILV
Tax Consulting	4	Masterarbeit
Tax Consulting	4	Masterprüfung | AP
Tax Consulting	4	Seminar zur Masterarbeit | SE
Tax Management	1	Spezialfragen des Finanz- und Wirtschaftsstrafrechts | ILV
Tax Management	1	Spezialfragen des Verfahrensrechts | ILV
Tax Management	1	Spezialfragen lohnabhängige Abgaben | ILV
Tax Management	1	Betriebswirtschaftliche Steuerlehre | ILV
Tax Management	1	Controlling & Unternehmenssteuerung, nachhaltige Unternehmensführung | SE
Tax Management	1	IFRS - International Financial Reporting Standards | ILV
Tax Management	1	Integrierte Fallbeispiele nach UGB und Steuerrecht | ILV
Tax Management	1	Spezialfragen des Einkommensteuerrechts I | VO
Tax Management	1	Spezialfragen des Körperschaftsteuerrechts | ILV
Tax Management	1	Investitions- und Finanzierungsrechnung | ILV
Tax Management	2	Gemeinnützigkeitsrecht | VO
Tax Management	2	Verkehrssteuern, ausgewählte sonstige Abgaben | VO
Tax Management	2	Konsolidierung | ILV
Tax Management	2	Datensicherheit | VO
Tax Management	2	Englisch als Fachsprache 1 | ILV
Tax Management	2	Spezialfragen des Einkommensteuerrechts II | SE
Tax Management	2	Unternehmensbewertung | ILV
Tax Management	2	Unionsrecht | SE
Tax Management	2	Verfassungsrecht | SE
Tax Management	2	Unternehmens- und Gesellschaftsrecht | ILV
Tax Management	2	Spezialfragen des Umsatzsteuerrechts national | ILV
Tax Management	2	Forschungsmethodik | ILV
Tax Management	2	Schreiben im wissenschaftlichen Kontext | SE
Tax Management	3	Organisationsentwicklung, Personalentwicklung und Führung im digitalen Zeitalter | ILV
Tax Management	3	E-Government-Anwendungen | VO
Tax Management	3	Internationale Datenbanken | ILV
Tax Management	3	Englisch als Fachsprache 2 | ILV
Tax Management	3	OECD-Richtlinien und Musterabkommen (inkl. DBA) | SE
Tax Management	3	OECD-Verrechnungspreise | SE
Tax Management	3	Österreichisches Außensteuerrecht und Rechtsvergleiche | SE
Tax Management	3	Masterarbeit
Tax Management	3	Themenfindung Masterarbeit - die Forschungsfrage | SE
Tax Management	3	Umgründungssteuerrecht | ILV
Tax Management	3	Umsatzsteuerrecht im Binnenmarkt | ILV
Tax Management	4	Big Data und Datenmanagement | ILV
Tax Management	4	Digital Transformation Process | ILV
Tax Management	4	Digitale Toolbox für Tax-Manager*innen | ILV
Tax Management	4	Risk Analysis | ILV
Tax Management	4	Masterarbeit
Tax Management	4	Seminar zur Masterarbeit | SE
Tax Management	4	Masterprüfung
Tax Management	4	Masterprüfung | AP
Technical Management	1	Advanced Business English | ILV
Technical Management	1	Advanced Project Management | ILV
Technical Management	1	European Product Liability, Patent and Trademark Law | VO
Technical Management	1	Organization and Management | ILV
Technical Management	1	Product Management and Product Life Cycle Management | ILV
Technical Management	1	Finance and Accounting | ILV
Technical Management	1	Probability and Statistics | ILV
Technical Management	1	AI in Production and Management | ILV
Technical Management	1	Technology and Knowledge Management | VO
Technical Management	2	Entrepreneurship and Innovation | ILV
Technical Management	2	European Competition Law | VO
Technical Management	2	Human Resources Management | VO
Technical Management	2	Intellectual Property Rights | VO
Technical Management	2	Advanced Quality Management | ILV
Technical Management	2	English for Entrepreneurs | ILV
Technical Management	2	Leadership Styles and Intercultural Aspects | SE
Technical Management	2	Negotiation, Rhetorics and Moderation | SE
Technical Management	2	Data Science Boot Camp | ILV
Technical Management	2	Data-Driven Optimization | ILV
Technical Management	2	Investment Decisions and Venture Capital | ILV
Technical Management	3	Requirements Engineering | VO
Technical Management	3	IT Security | ILV
Technical Management	3	Applied Requirements Engineering | ILV
Technical Management	3	Master Thesis Seminar I | SE
Technical Management	3	System Design and User Experience | VO
Technical Management	3	Cloud Computing Technologies | ILV
Technical Management	3	Computer Networks | ILV
Technical Management	3	Software Engineering | ILV
Technical Management	3	Software Quality and Test | ILV
Technical Management	3	Corporate Environmental and Energy Management | VO
Technical Management	3	Instruments of Environmental Policy | VO
Technical Management	3	Interdisciplinary Analysis in Environmental Protection | SE
Technical Management	3	Technology and Life Cycle Assessment | ILV
Technical Management	3	Use of Energy and Energy Efficiency | VO
Technical Management	3	Power Grids | VO
Technical Management	3	Renewable Energy Systems | VO
Technical Management	3	Electrical Power Generation | ILV
Technical Management	4	IT Project Management and Agile Methods | ILV
Technical Management	4	IT-Controlling | SE
Technical Management	4	Master Thesis Seminar II | SE
Technical Management	4	Masterprüfung | AP
Technical Management	4	Software and Internet Economics | ILV
Technical Management	4	Energy Economics | VO
Technical Management	4	Energy Economics | UE
Technical Management	4	Environmental Legislation | VO
Technical Management	4	Energy Markets and Regulations | VO
Technische Informatik	1	Embedded Plattforms | ILV
Technische Informatik	1	Internet of Things | VO
Technische Informatik	1	Programmable Logic Devices | ILV
Technische Informatik	1	Numerische Mathematik | ILV
Technische Informatik	1	Stochastik und Wahrscheinlichkeitsrechnung | ILV
Technische Informatik	1	Interfaces und Bussysteme | ILV
Technische Informatik	1	Wireless Connectivity | VO
Technische Informatik	1	Konzepte der Informatik 1 | ILV
Technische Informatik	2	Real Time Linux | ILV
Technische Informatik	2	RTOS - Enhanced uC Systems | ILV
Technische Informatik	2	Embedded Assembler-Programming | ILV
Technische Informatik	2	Embedded SW Testing | VO
Technische Informatik	2	Security Aspects of Embedded Systems | VO
Technische Informatik	2	Digitale Regelungstechnik | ILV
Technische Informatik	2	Digitale Signalverarbeitung | ILV
Technische Informatik	3	Computer Vision | ILV
Technische Informatik	3	KI im Embedded Bereich | ILV
Technische Informatik	3	Entrepreneur- und Leadership | ILV
Technische Informatik	3	Applications Industry 4.0 | PR
Technische Informatik	3	Safety Strategies | ILV
Technische Informatik	3	Konzepte der Informatik 2 | ILV
Technische Informatik	4	KI im wissenschaftlichen Kontext | SE
Technische Informatik	4	Ethik, Technik und Gesellschaft | SE
Technische Informatik	4	Masterkolloquium | AP
Zytologie	1	Studium starten | SE
Zytologie	1	Anatomie | VO
Zytologie	1	Pathologie | VO
Zytologie	1	Histologie | VO
Zytologie	1	Wissenschaftliches Arbeiten und Literaturrecherche | SE
Zytologie	1	Evidenzbasierte Praxis und Critically Appraised Topic | SE
Zytologie	1	Grundlagen der gynäkologischen Zytologie & Zytomorphologie | ILV
Zytologie	1	Klassifikation, Befundpraxis & Troubleshooting | ILV
Zytologie	1	Grundlagen & Zytologie der Lunge und Harntraktzytologie | ILV
Zytologie	1	Zytologie der serösen Höhlen, Zysten & Gelenksergüsse | ILV
Zytologie	1	Präanalytische Grundlagen | VO
Zytologie	1	Aufbereitungsmethoden & Färbetechniken | ILV
Zytologie	1	Einführung in die Digitale Pathlogie/ Zytologie und Whole-Slide-Imaging | VO
Zytologie	1	IT- und AI-Grundlagen in der Digitalen Pathologie/ Zytologie | ILV
Zytologie	2	Komplexe dysplastische Zellbilder & Analzytologie | ILV
Zytologie	2	Glanduläre Zellbilder | ILV
Zytologie	2	Zytologie-Histologie-Kolposkopie-Molekularpathologie | SE
Zytologie	2	Berufsbild und rechtliche Grundlagen in der Histopathologie und Zytologie | ILV
Zytologie	2	Zytologie & Histopathologie: Internationale Standards, Freiberuflichkeit und Schnittstellenmanagement | SE
Zytologie	2	Qualitätsmanagement in der histo- und zytologischen Diagnostik: Von der Akkreditierung zur digitalen Absicherung | ILV
Zytologie	2	Zytologie der Schilddrüse & Brustdrüse | ILV
Zytologie	2	Zytologie des Gastrointestinaltrakts, Leber & Pankreas | ILV
Zytologie	2	Liquorzytologie | ILV
Zytologie	2	Lymphknoten & Knochenmarkzytologie & Zytologie des Auges | ILV
Zytologie	2	Datenauswertung in der Pathologie | SE
Zytologie	2	Forschungsprojekt-Simulation "Von der Idee zur Studie" | SE
Zytologie	2	Journal Club Pathologie/ Zytologie | SE
Zytologie	2	Troubleshooting in der Präanalytik & Forensische Zytologie | SE
Zytologie	2	Fallmanagement, Befundkorrelation & Ableitung von Maßnahmen | SE
Zytologie	2	Troubleshooting in der Präanalytik | SE
Zytologie	2	Fallmanagement und ROSE-basierte Entscheidungsfindung | SE
Zytologie	2	Erweiterte immunzytochemische Diagnostik und molekulare Diagnostik spezieller Fragestellungen | VO
Zytologie	2	Immunzytochemie in der Zytologie (PAP, Dünnschicht, EBUS, FNA) | SE
Green Mobility	1	Energiespeicher
Green Mobility	1	Energiemanagement im Fahrzeug
Green Mobility	1	Antriebsstrang
Green Mobility	2	Systemelektronik
Green Mobility	2	Bussysteme und Datenkommunikation
Green Mobility	2	Safety im Automotive Bereich
Green Mobility	3	Ladetechnologien und Ladeinfrastruktur
Green Mobility	3	Mobilitätskonzepte
Green Mobility	4	Projekt-, Prozess- und Qualitätsmanagement
Green Mobility	4	Rechtliche Aspekte der Elektromobilität
Technische Gebäudeausstattung	1	Erneuerbare Energiegewinnung und Wasserversorgung
Technische Gebäudeausstattung	1	Fotovoltaik, Erdwärme und Solarthermie
Technische Gebäudeausstattung	1	Elektrische Netze und Systeme in Gebäuden
Technische Gebäudeausstattung	2	Steuerung und Überwachung technischer Anlagen
Technische Gebäudeausstattung	2	Versorgungsanlagen und Installationen
Technische Gebäudeausstattung	2	Gebäudeausstattungsprojekt
            """.trimIndent()
        )
        append('\n')
}

private fun parseAcademicLectureCatalog(rows: String): Map<String, Map<Long, List<String>>> {
    val catalog = linkedMapOf<String, MutableMap<Long, MutableList<String>>>()

    rows.lineSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .forEach { row ->
            val parts = row.split('	', limit = 3)
            if (parts.size != 3) {
                return@forEach
            }

            val studyPath = parts[0]
            val semester = parts[1].toLongOrNull() ?: return@forEach
            val lectureName = parts[2]
            val studyCatalog = catalog.getOrPut(studyPathCatalogKey(studyPath)) { linkedMapOf() }
            val lectures = studyCatalog.getOrPut(semester) { mutableListOf() }

            if (lectureName !in lectures) {
                lectures.add(lectureName)
            }
        }

    return catalog.mapValues { (_, semesters) ->
        semesters.mapValues { (_, lectures) -> lectures.toList() }
    }
}
