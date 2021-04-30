# studyHelper

Pokretanje programa:

Postoji nekoliko nacina za pokretanje programa i oni ce biti navedeni po tezini.

1. Ne razumem se ja. Hocu samo da kliknem dva puta i otvorim
   
    - Preuzmite softver sa linka (), 
    - raspakujte ga i 
    - pokretine xxx.exe. 
    - Ne brisite nista iz tog foldera.


2. Razumem se nesto ja tu malo. Imam i maven

    - Preuzmite code sa github-a,
    - raspakujte ga,
    - otvorite glavni folder i
    - u commandline kucajte: mvn clean validate compile javafx:run 


3. Ja sam kolega programer. Hteo bih da pokrenem kod u svom IDE.
   
    - File / Project / Project SDK / minimum: 9.0.4
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