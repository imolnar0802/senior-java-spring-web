Feladat
=======
Valósítsd meg a webböngészőn keresztül feltöltött képek tárolását és digitális aláírását. A rendszer JavaScript frontend-del és Java backend-del rendelkezik, melyek közül a fájl feltöltés kerete már implementálásra került. A tárolás és aláírás megoldása viszont már a Te feladatod. Az aláíráshoz használandó kulcsot a projekt könyvtárában, PKCS8 formátumban találod. Az aláírás SHA256withRSA algoritmussal legyen megvalósítva, majd ennek eredménye BASE64 algoritmus használatával legyen kódolva. Az így kapott eredmény legyen megjelenítve a frontend-den is. A feltöltött képek maximális mérete 2MB lehet, típusára vonatkozólag nincs megkötés.
Fontos, hogy a későbbiekben legyen lehetőség az alkalmazás továbbfejlesztésére, valamint a kód legyen érthető más fejlesztők számára is.

Fejlesztési tudnivalók
======================
Az alkamazás Spring Boot keretrendszeren alapszik és `mvn spring-boot:run` paranccsal vagy a Main fálj futtatásával lehet indítani. Sikeres build-et követően a http://localhost:8080 url-en érhető el a fáljfeltöltő komponens és a listázó, mely kezdeti állapotban üres. Fálj felöltés drag&drop használatával vagy kattintással és fálj kiválasztással lehetséges.
Új fálj feltöltése a "New upload" gomb megnyomása után lehetséges.

A feladat elvégézéshez biztosított fáljok melyek a projektben megtalálhatók:
    * PKCS8 aláíró kulcs
    * Néhány teszt fálj és a hozzájuk tartozó BASE64 kódolt aláírás
	
Kiegészítések
=============
 - A képek listázásához és fáljfeltöltéshez szükséges implementálni a klienst kiszolgáló végpontokat.
 
Az alábbi képen egy példát kívánunk mutatni a helyes megoldásra vonatkozólag:
 ![Solution](image/example.png)