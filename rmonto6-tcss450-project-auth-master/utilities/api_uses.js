
const express = require('express')

//retrieve the router object from express
var router = express.Router()
const api_keys = {
    keys : [{
        key1: "0262b79849e84a35866dc2600904ecd3"
    },
    {
        key2: "188f548df07c4b31a4f9ba8663aaeb42"
    },
    {
        key3: "09684b49b5344c0a87cd1961aa60b05c"
    },
    {
        key4: "61a3155487884405a26387bc46edd87f"
    }
]}

router.get("/", (request, response) => {
        keys = JSON.stringify(api_keys.keys[3])
        position = 3
        split = keys.split('\"')
        fetch('https://api.weatherbit.io/v2.0/subscription/usage?key='+ split[3])
    .then(response =>{
        return response.json();
    })
    .then(data =>{
        console.log(data.calls_remaining)
        

        response.send({
            //req.query is a reference to arguments a
            message: position
        })
})
    
})




module.exports = router 