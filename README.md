**OpenPGP Package for WebMethods**
The OpenPGP Package provides encryption, decryption, signing, and verification functionalities based on the OpenPGP standard. This package seamlessly integrates with Software AG's WebMethods Integration Server, ensuring secure communication and data protection using industry-standard cryptographic techniques.

**Features**
PGP Encryption – Encrypt messages using public-key cryptography.
PGP Decryption – Decrypt messages using the recipient's private key.
Digital Signing – Sign messages to ensure authenticity and integrity.
Signature Verification – Verify digital signatures to confirm data integrity.
Key Management – Import, export, and manage PGP key pairs.
Seamless Integration – Designed for smooth operation within the WebMethods Integration Server environment.

**Prerequisites**
Before installing and using the WebMethods OpenPGP package, ensure that:

WebMethods Integration Server (IS) 10.x or later is installed.
Java JDK 8 or later is available.
The required OpenPGP libraries (e.g., Bouncy Castle) are included in the classpath.
Installation
Download the Package

Clone or download the package from this repository.
Deploy to WebMethods IS

Copy the package directory into /packages/.
Restart the WebMethods Integration Server.
Enable the Package

Navigate to Packages > Management in the IS Admin Console.
Activate the OpenPGP package.
