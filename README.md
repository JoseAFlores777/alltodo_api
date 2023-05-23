## Secrets Properties Configuration

The `secrets.properties` file contains sensitive information and should not be included in the source code repository. Follow the instructions below to create your own `secrets.properties` file based on the example provided.

1. Duplicate `secrets.properties.example`:
    - Duplicate the `secrets.properties.example` file in the project directory.
    - Rename the duplicated file to `secrets.properties`.

2. Update the property values:
    - Open the `secrets.properties` file.
    - Replace the placeholder values with your actual secret information.

3. SMTP Configuration:
    - The example `secrets.properties` file contains SMTP configuration properties for using SendGrid as the mail service provider.
    - Update the following properties based on your SMTP provider:
        - `secret.mail.host`: The SMTP server host.
        - `secret.mail.port`: The SMTP server port.
        - `secret.mail.username`: Your email address or username.
        - `secret.sendgrid.api.key`: Your SendGrid API key.
        - `secret.mail.password`: Your