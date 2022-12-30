const nodemailer = require('nodemailer')
const { google } = require('googleapis')

const CLIENT_ID = '254939617338-9o8oqdobsabdd5bkedakd31mnvdjagel.apps.googleusercontent.com'
const CLIENT_SECRET = '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'
const REDIRECT_URI = 'https://developers.google.com/oauthplayground'
const REFRESH_TOKEN = '1//04IX233oN_Lb4CgYIARAAGAQSNwF-L9IrXzBQdkRw-BlrwzTqtLdB5Qx8slnkHiOXFb3YNgTGuJs8eLVE-n_1_UuUCIjov2VeO9U'

const oAuth2Clien  = new google.auth.OAuth2(CLIENT_ID,CLIENT_SECRET, REDIRECT_URI)
oAuth2Clien.setCredentials({ refresh_token: REFRESH_TOKEN })

const FROM = 'Group7_TCSS450_admin @ <emailfortrialcoding@gmail.com>'
const TOEMAIL = 'nickungdung@gmail.com'
const SUBJECT = 'Example to send email through API gmail'
const BODY = ' information need to send in email '



async function sendMail(){

    try{
        const accessToken = await oAuth2Clien.getAccessToken()

        const transport = nodemailer.createTransport({
            service: 'gmail',
            auth: {
                type: 'OAuth2',
                user: 'emailfortrialcoding@gmail.com',
                clientId: CLIENT_ID,
                clientSecret: CLIENT_SECRET,
                refreshToken: REFRESH_TOKEN,
                accessToken: accessToken
            }
        })
    
        const mailOptions = {
            from: FROM,
            to: TOEMAIL,
            subject: SUBJECT,
            text: BODY,
        //    html: '<h1> Hello form gmail email using API </h1>',
        };
    
        const result = await transport.sendMail(mailOptions);
        return result;

    } catch(error){
        return error
    }
} 

sendMail()
    .then((result) => console.log('email send ....', result))
    .catch( (error) => console.log(error.message) );