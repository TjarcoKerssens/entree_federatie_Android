# Entree Federatie Native login
Deze repository bevat twee applicaties die als referentie gebruikt kunnen worden om inloggen in een native iOS applicatie mogelijk te maken. Er is ook een voorbeeld voor een native iOS Applicatie.

## Werking
De applicaties gebruiken een WebView om het login scherm te tonen. Vervolgens worden de cookies als name-value pairs uit deze view gehaald en in een file opgeslagen. Op het moment dat de applicatie opstart, wordt getest of dit bestand met een geldige sessie bestaat. Als dit zo is, wordt er in een keer ingelogd. Anders wordt het inlogscherm getoond. 

Met het toepassen van een [`sharedUserId`](https://developer.android.com/guide/topics/manifest/manifest-element#uid) is het mogelijk om de file met de sessie te delen met een andere applicatie, mits deze applicatie met dezelfde key gesigned wordt. Echter lukt het niet om de sessie voledig te herstellen in deze andere applicatie, wat SSO op Android momenteel niet mogelijk maakt. 

### Referentie applicatie
De [referentie applicatie](https://referentie.entree.kennisnet.nl/) is een simpele applicatie die wat eigenschappen van de ingelogde gebruiker toont. In dit voorbeeld wordt deze applicatie gebruikt om in te loggen en vervolgens de eigenschappen uit de applicatie te halen om in de native applicatie te tonen. 

### WikiWijs applicatie
Voor WikiWijs is de implementatie enkel een WebView. Deze applicatie kan uitgebreid worden om de SSO functionaliteit te implementeren. Hier is een start voor gemaakt door te tonen hoe een gedeelde `Context` gecreeerd kan worden om de sessie van de Referentie Applicatie te lezen. Echter lukt het niet om de cookies uit deze sessie te laten werken met de WikiWijs applicatie .

## Gebruik
De classes in de map `data` en `models` kunnen gebruikt worden om een eigen implementatie te maken waarmee gebruik kan worden gemaakt om in te loggen. Kijk naar de `LoginActivity` class om te zien hoe deze gebruikt kunnen worden en welke properties er nodig zijn op de WebView