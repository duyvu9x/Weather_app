//express is the framework we're going to use to handle requests
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

router.post("/", (request, response) => {
    const lat = request.body.lat;
    const long = request.body.long;
    const zip = request.body.zip;
    if(zip == null){
    fetch('https://rmonto6-tcss450-project-auth.herokuapp.com/api_uses')
    .then(response =>{
        return response.json();
    })
    .then(data =>{
        keys = JSON.stringify(api_keys.keys[data.message])
        split = keys.split('\"')
    fetch('https://api.weatherbit.io/v2.0/current?lat='+lat+'&lon='+long+'&key=' + split[3])
        .then(response =>{
            return response.json();
        })
        .then(data =>{
            const payload = {
                temp: data.data[0].temp,
                clouds: data.data[0].clouds,
                aqi: data.data[0].aqi,
                weather: data.data[0].weather.description
            }
            
            response.send({
                //req.query is a reference to arguments a
                message: payload
            })
    })
    
})
    }else{
        fetch('https://rmonto6-tcss450-project-auth.herokuapp.com/api_uses')
    .then(response =>{
        return response.json();
    })
    .then(data =>{
        keys = JSON.stringify(api_keys.keys[data.message])
        split = keys.split('\"')
    fetch('https://api.weatherbit.io/v2.0/current?lat='+'&postal_code='+zip+'&key=' + split[3])
        .then(response =>{
            return response.json();
        })
        .then(data =>{
            const payload = {
                temp: data.data[0].temp,
                clouds: data.data[0].clouds,
                aqi: data.data[0].aqi,
                weather: data.data[0].weather.description
            }
            
            response.send({
                //req.query is a reference to arguments a
                message: payload
            })
    })
    
})
    }
})




module.exports = router 