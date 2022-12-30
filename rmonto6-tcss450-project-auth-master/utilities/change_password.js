//express is the framework we're going to use to handle requests
// const { response } = require('express')
// const { request } = require('express')
// const express = require('express')
// const pool = require('../utilities').pool

// const validation = require('../utilities').validation

// let isStringProvided = validation.isStringProvided
// const jwt = require('jsonwebtoken')
// const router = express.Router()
// const generateHash = require('../utilities').generateHash
// const generateSalt = require('../utilities').generateSalt

const express = require("express");

const jwt = require("jsonwebtoken");

const router = express.Router();
const { generateHash, generateSalt } = require("../utilities");
const { isValidPassword } = require("../utilities/validationUtils");
const pool = require("../utilities").pool;

// const config = {
//     secret: process.env.JSON_WEB_TOKEN
// }

/**
 * @api {get} /auth Request to sign a user in the system
 * @apiName GetAuth
 * @apiGroup Auth
 * 
 * @apiHeader {String} authorization "username:password" uses Basic Auth 
 * 
 * @apiSuccess {boolean} success true when the name is found and password matches
 * @apiSuccess {String} message "Authentication successful!""
 * @apiSuccess {String} token JSON Web Token
 * 
 *  * @apiSuccessExample {json} Success-Response:
 *     HTTP/1.1 201 OK
 *     {
 *       "success": true,
 *       "message": "Authentication successful!",
 *       "token": "eyJhbGciO...abc123"
 *     }
 * 
 * @apiError (400: Missing Authorization Header) {String} message "Missing Authorization Header"
 * 
 * @apiError (400: Malformed Authorization Header) {String} message "Malformed Authorization Header"
 * 
 * @apiError (404: User Not Found) {String} message "User not found"
 * 
 * @apiError (400: Invalid Credentials) {String} message "Credentials did not match"
 * 
 */ 
 router.post("/", (request, response, next) => {
    const jwtBody = request.body.jwt;
    const jwtDecoded = jwt.decode(jwtBody);
    const memberid = jwtDecoded.memberid;
    const oldPassword = request.body.oldPassword;
    const newPassword = request.body.newPassword;

    //Verify that the user supplied a valid new password
    if (!isValidPassword(newPassword)) {
        response.status(400).send({
            message: "Password does not meet requirements",
        });
        return;
    }

    // Gets users salted hash, salt, and memberid
    let getSaltQuery =
        "SELECT CREDENTIALS.SALTEDHASH, CREDENTIALS.SALT FROM CREDENTIALS WHERE CREDENTIALS.MEMBERID = $1";

    const values = [memberid];
    pool.query(getSaltQuery, values)
        .then((result) => {
            // If user email doesnt exist, return error
            if (result.rowCount == 0) {
                response.status(404).send({
                    message: "User not found",
                });
                return;
            }

            // If the user does exist, get salt and salted hash to
            // compare with old password that was sent
            if (result.rowCount == 1) {
                const salt = result.rows[0].salt;
                const salted_hash = generateHash(oldPassword, salt);

                // If the old password is correct, then the password can
                // be updated in the database with the new password
                if (salted_hash == result.rows[0].saltedhash) {
                    const newSalt = generateSalt(32);
                    const newSaltedHash = generateHash(newPassword, newSalt);
                    let insertNewPassQuery =
                        "UPDATE CREDENTIALS SET SALT = $1, SALTEDHASH = $2 WHERE CREDENTIALS.MEMBERID = $3";
                    const newValues = [newSalt, newSaltedHash, memberid];
                    pool.query(insertNewPassQuery, newValues)
                        .then((result) => {
                            // Succesfullu updated password
                            if (result.rowCount == 1) {
                                response.status(200).send({
                                    message: "Password updated",
                                });
                                return;
                            }
                            // We should never reach this
                            // If multiple accounts got their password updated
                            // or none at all
                            else {
                                response.status(400).send({
                                    message: "Error has occured",
                                });
                                return;
                            }
                        })
                        // If the query somehow fails
                        .catch((error) => {
                            response.status(500).send({
                                message: "Database query failed #1",
                            });
                            return;
                        });
                }
                // Invalid old password input by user
                else {
                    response.status(400).send({
                        message: "Invalid old password",
                    });
                    return;
                }
            }
        })
        // Failed to ger users salted hash
        .catch((error) => {
            response.status(500).send({
                message: "Database query failed #2",
            });
            return;
        });
});

module.exports = router;