//express is the framework we're going to use to handle requests
const express = require('express')

//Access the connection to Heroku Database
const pool = require('../utilities/exports').pool

const router = express.Router()

const validation = require('../utilities/exports').validation
let isStringProvided = validation.isStringProvided

/**
 * @apiDefine JSONError
 * @apiError (400: JSON Error) {String} message "malformed JSON in parameters"
 */ 

/**
 * @api {post} /contacts Request to add a chat
 * @apiName PostContacts
 * @apiGroup Contacts
 * 
 * @apiHeader {String} authorization Valid JSON Web Token JWT
 * @apiParam {String} name the name for the chat
 * 
 * @apiSuccess (Success 201) {boolean} success true when the name is inserted
 * @apiSuccess (Success 201) {Number} chatId the generated chatId
 * 
 * @apiError (400: Unknown user) {String} message "unknown email address"
 * 
 * @apiError (400: Missing Parameters) {String} message "Missing required information"
 * 
 * @apiError (400: SQL Error) {String} message the reported SQL error details
 * 
 * @apiError (400: Unknown Chat ID) {String} message "invalid chat id"
 * 
 * @apiUse JSONError
 */ 
router.post("/", (request, response, next) => {
    const username_A = request.body.username_A
    const username_B = request.body.username_B
    if(username_A == username_B) {
        response.send({
            message: "Contact cannot be the same",
            //error: err

        })
        return;
    }
    const verified = 1;
    //if contact exists 
    let select1 = `  SELECT contacts.MemberID_A, contacts.MemberID_B
                    FROM contacts, members A, members B
                    WHERE MemberID_A = A.MemberID AND MemberID_B = B.MemberID
                    AND A.Username = $1 AND B.Username= $2`

    let values = [username_A, username_B]
    pool.query(select1, values)
        .then(result => {
            if (result.rowCount == 0) {   
                next()
            } else {
                response.send({
                    message: "Contact already exists",
                    //error: err
    
                })
            }
        }).catch(error => {
            response.status(400).send({
                message: "SQL Error",
                error: error
            })
        })
    },  (request, response, next) => {
        const username_A = request.body.username_A
        const username_B = request.body.username_B
        let select1 = `  SELECT contacts.MemberID_A, contacts.MemberID_B
                    FROM contacts, members A, members B
                    WHERE (MemberID_A = A.MemberID AND MemberID_B = B.MemberID
                    AND A.Username = $1 AND B.Username= $2 AND verified = '1')
                    OR (MemberID_A = A.MemberID AND MemberID_B = B.MemberID
                        AND A.Username = $2 AND B.Username= $1 AND verified = '1')`

        let values = [username_A, username_B]
        pool.query(select1, values)
        .then(result => {
            if (result.rowCount == 0) {   
                next()
            } else {
                response.send({
                    message: "Contact already exists",
                    //error: err
    
                })
            }
        }).catch(error => {
            response.status(400).send({
                message: "SQL Error",
                error: error
            })
        })
    },  (request, response, next) => {

        
    const username_A = request.body.username_A
    const username_B = request.body.username_B
    const verified = 1;
    let select = `  SELECT contacts.MemberID_A, contacts.MemberID_B
                    FROM contacts, members A, members B
                    WHERE MemberID_A = A.MemberID AND MemberID_B = B.MemberID
                    AND A.Username = $2 AND B.Username= $1`
    let values = [username_A, username_B]
    pool.query(select, values)
        .then(result => {
            if (result.rowCount > 0) {
                response.send({
                    //message: "contact exist",
                    success: true,
                    //memberID_A:result.rows[0].memberID_A,
                    //memberID_B:result.rows[0].memberID_B
    
                })
                let query = `UPDATE contacts
                                SET Verified = $1
                                WHERE contacts.MemberID_A = (SELECT members.MemberID
                                            FROM members
                                            WHERE members.Username = $2) 
                                AND contacts.MemberID_B = (SELECT members.MemberID
                                            FROM members
                                            WHERE members.Username = $3); `
                let values = [verified, username_B, username_A]
                pool.query(query, values)

            } else {
                //contact not found so create it
                next()
            }
        }).catch(error => {
            response.status(400).send({
                message: "SQL Error",
                error: error
            })
        })
    //check if already made update
}, (request, response) => {
    const username_A = request.body.username_A
    const username_B = request.body.username_B
    let insert = `INSERT INTO contacts(MemberID_A, MemberID_B)
                    SELECT A.MemberID, B.MemberID
                    FROM members A, members B
                    WHERE A.Username = $1 AND B.Username = $2`
    let values = [username_A, username_B]
    pool.query(insert, values)
        .then(result => {
            response.send({
                success: true,
                //memberID_A:result.rows[0].memberID_A,
                //memberID_B:result.rows[0].memberID_B

            })
        }).catch(err => {
            response.status(400).send({
                message: "SQL Error",
                error: err
            })

        })
})

router.get("/", (request, response, next) => {
    //validate on missing or invalid (type) parameters
    //validate chat id exists
    const username_A = request.body.username_A

    let query = `SELECT A.Username as "1st user", B.Username as "2nd User", verified
    FROM contacts, members A, members B
    WHERE (MemberID_A = A.MemberID AND MemberID_B = B.MemberID
    AND A.Username = $1) or (MemberID_A = A.MemberID AND MemberID_B = B.MemberID
    AND B.Username = $1)`    
    let values = [username_A]

    pool.query(query, values)
        .then(result => {
            if (result.rowCount > 0) {
                response.send({
                    success: true,
                    rows: result.rows

                })
            }
        }).catch(error => {
            response.status(400).send({
                message: "SQL Error",
                error: error
            })
        })
})

router.delete("/", (request, response, next) => {
  
    const username_A = request.body.username_A
    const username_B = request.body.username_B
    let query = `SELECT * FROM contacts
                WHERE (MemberID_A = (SELECT members.MemberID
                                    FROM members
                                    where members.Username = $1) 
                        AND MemberID_B = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $2)) 
                        OR (MemberID_A = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $2) 
                        AND MemberID_B = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $1)) ;`
    let values = [username_A, username_B]

    pool.query(query, values)
    .then(result => {
        if (result.rowCount == 0) {
            response.send({
                message: "contact does not exist to delete",
                //success: true,

            })

        } else {
            //contact not found so create it
            next()
        }
    }).catch(error => {
        response.status(400).send({
            message: "SQL Error",
            error: error
        })
    })                          

}, (request, response) => {
    const username_A = request.body.username_A
    const username_B = request.body.username_B
    let query = `DELETE FROM contacts
                WHERE (MemberID_A = (SELECT members.MemberID
                                    FROM members
                                    where members.Username = $1) 
                        AND MemberID_B = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $2)) 
                        OR (MemberID_A = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $2) 
                        AND MemberID_B = (SELECT members.MemberID
                                        FROM members
                                        where members.Username = $1)) ;`
    let values = [username_A, username_B]

    pool.query(query, values)
    .then(result => {
        response.send({
            success: true
        })
    }).catch(err => {
        response.status(400).send({
            message: "SQL Error",
            error: err
        })
    })
})

module.exports = router