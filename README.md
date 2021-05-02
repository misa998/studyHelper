# studyHelper

Ovaj program namenjen je ljudima koji zele da efikasnije uce i prate svoje obaveze.\
  Unesite svoje predmete, rokove za zavrsetak i obaveze koje imate i svakog predmeta;\
  Koristite Pomodoro tehniku i vreme utroseno na ucenje ce biti zabelezeno. To Vas moze znatno motivisati za dalji rad.\
  Tu je i tab za motivaciju, gde mozete upisati neke savete, algoritam za resavanje problema ili neke reci motivacije za dalji rad.

Pokretanje programa:

Postoji nekoliko nacina za pokretanje programa i oni ce biti navedeni po tezini.

1. Najlaksi nacin (dupli klik)
   
    - Preuzmite zip sa linka (https://drive.google.com/drive/folders/1DhiPSd9CcLV-iEh9dhHzxOdNltoHoeR6?usp=sharing), 
    - raspakujte zip (Desni klik / Extract) i 
    - pokrenite StudyHelper-1.0.exe. 


2. Tezi nacin (maven)

    - Potreban je minimum jdk-11.
    - Preuzmite code sa github-a,
    - otvorite glavni folder i
    - u commandline kucajte: mvn clean validate compile javafx:run 


3. Najtezi nacin (project import)
   
   Za IntelliJ, u slucaju da se projekat ne pokrene odmah:
    - File / Project / Project SDK / minimum: 11.0.1
    - File / Modules / Sources
        - Add Content Root / selektovati glavni folder projekta
        - odabrati src\main\java kao Sources Folders, klikom na folder java, pa iznad Sources
        - odabrati src\test\java kao Test
        - odabrati src\main\resources kao Resources
        - src\main kao Excluded
        - target kao Excluded
    - File / Modules / Dependencies
        - Add (Alt+Insert) / Library... / selektovati sve osim Global library i dodati
    - Run / Edit configuration...
        - Add New Configuration / Application / postaviti Main class da bude NewMain (com.studyhelper)
