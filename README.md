# studyHelper

Pokretanje programa:

Postoji nekoliko nacina za pokretanje programa i oni ce biti navedeni po tezini.

1. Najlaksi nacin
   
    - Preuzmite softver sa linka (), 
    - raspakujte ga i 
    - pokrenite StudyHelper-1.0.exe. 


2. Tezi nacin

    - Preuzmite code sa github-a,
    - otvorite glavni folder i
    - u commandline kucajte: mvn clean validate compile javafx:run 


3. Najtezi nacin
   
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