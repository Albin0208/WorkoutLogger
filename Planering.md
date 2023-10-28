# Tekniska krav
- Modulär kod, enligt någon vald princip. Referera till best practices, t.ex. ange i kommentarer eller i Readme-filen länk till websida eller annan källa du har valt att följa. Kika t.ex på designmönstret Model View ViewModel (MVVM) som numera förordas av Google för Android-utveckling. (2 p)
- Sökningsfunktion i appen, dvs. att användaren kan skriva in ett sökord och få upp relavant information. Oftast krävs att sökningen genomförs på backend-sidan, för att säkerställa att all relevant information kan visas. (2 p)
- Hantera, dvs. hålla koll på inloggningsstatus hos olika användare, så att bara inloggade användare kommer åt att använda vissa features, eller att inloggning krävs för att få se viss information i appen. (1 p)

# Entreprenöriella krav
- Firestore. Använder Cloud Firestore eller Realtime Database som databas. Båda lösningarna är Googles lagringstjänst för mobile- och webb-appar som erbjuder bl.a. databas och autentisering. Dessutom finns en mer generell databas-lösning som heter Firebase Cloud Storage. Cloud Storage accepterar både ljud, bild, och text så i princip alla appar kan utnyttja Cloud Storage på ett eller annat sätt. (OBS! 2 p)
- Kontohantering, t.ex. lagra poäng, vänlista, eller dylikt som ska sparas över tid för olika användare (1 p)
- Enkel inloggning av användare mot Firebase (1 p)
- Tredjepartsinloggning med Google eller Facebook. Notera att man också kan använda Firebase för att implementera dessa autentiseringsmetoder, men ni får bara poäng för en implementation av varje typ (d.v.s det ger inte två poäng att implementera Facebook-autentisering på två olika sätt) (1 p)
- Använder Google maps på meningsfullt sätt i appen. Man behöver registrera ett konto med ett bankkort för att kunna använda Google maps i sin applikation, men maps i sig ska fortfarande vara helt gratis (åtminstone så länge det inte är flera hundra tusen anrop per månad). Google har en mer detaljerad pristabell. Om man, av förståeliga skäl, är obekväm med att ge Google sitt kortnummer så accepterar vi också lösningar baserade på OpenStreetMap. Ni har mycket frihet i hur ni använder maps-funktionen,
- Multispråk-stöd. Använd Androids system för att göra så att Appen identiferar minst 2 språk och använder t.ex. Engelska som default och Svenska för användare med Svenska språkinställningar. (1 p)

# Tidsplanering

## Vecka 40
- Loginskärm med uppkoppling mot firebase
## Vecka 41
- Fragment som listar alla övningar som finns i databasen
- Användaren ska kunna skapa egen övning (OBS! Troligtvis inte användarspecifika övningar ännu)
## Vecka 42
- Fixa egna övningar i databasen så att de är användarspecifika
- Skapa en knapp som startar ett pass
- Skapa en grundläggande struktur för hur träning av ett pass ser ut
## Vecka 43
- Det ska gå att lägga till övningar i träningsvyn
- Set ska kunna läggas till
- Ett set ska visas som reps och vikt
## Vecka 44
- Det ska gå att ta bort övningar från träningsvyn och set från övningar
- Set ska kunna markeras som klara
- Börja implementera hur ett pass avslutas och datan sparas i databasen
## Vecka 45
- Skapa en rekordvy där man kan se alla rekord man gjort i olika övningar
## Vecka 46
- En vy för att visa sina tidigare tränadepass
## Vecka 47
- Träningspass klart och redo för att visas med screencast
## Vecka 48
- Implementera sökfunktion för att söka efter övningar
## Vecka 49
- Implementera google maps
## Vecka 50
- Implementera löprunda
## Vecka 51
- Implementera löprunda
## Vecka 52
- Se över det som är kvar och färdigställa det
## Vecka 1
- Se över det som är kvar och färdigställa det
## Vecka 2
- Projektet klart och inlämnat
