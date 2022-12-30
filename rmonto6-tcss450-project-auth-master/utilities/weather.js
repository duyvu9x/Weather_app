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
    fetch('https://api.weatherbit.io/v2.0/forecast/daily?lat='+lat+'&lon='+long+'&key=' + split[3])
        .then(response =>{
            return response.json();
        })
        .then(data =>{
            const payload = {
                "days" : [
                    {
                        date: data.data[0].valid_date,
                        max: data.data[0].max_temp,
                        min: data.data[0].min_temp,
                        description: data.data[0].weather.description
                    },
                    {
                        date: data.data[1].valid_date,
                        max: data.data[1].max_temp,
                        min: data.data[1].min_temp,
                        description: data.data[1].weather.description
                    },
                    {
                        date: data.data[2].valid_date,
                        max: data.data[2].max_temp,
                        min: data.data[2].min_temp,
                        description: data.data[2].weather.description
                    },
                    {
                        date: data.data[3].valid_date,
                        max: data.data[3].max_temp,
                        min: data.data[3].min_temp,
                        description: data.data[3].weather.description
                    },
                    {
                        date: data.data[4].valid_date,
                        max: data.data[4].max_temp,
                        min: data.data[4].min_temp,
                        description: data.data[4].weather.description
                    },
                    {
                        date: data.data[5].valid_date,
                        max: data.data[5].max_temp,
                        min: data.data[5].min_temp,
                        description: data.data[5].weather.description
                    },
                    {
                        date: data.data[6].valid_date,
                        max: data.data[6].max_temp,
                        min: data.data[6].min_temp,
                        description: data.data[6].weather.description
                    }
                ]
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
    fetch('https://api.weatherbit.io/v2.0/forecast/daily?lat='+'&postal_code='+zip+'&key=' + split[3])
        .then(response =>{
            return response.json();
        })
        .then(data =>{
            const payload = {
                "days" : [
                    {
                        date: data.data[0].valid_date,
                        max: data.data[0].max_temp,
                        min: data.data[0].min_temp,
                        description: data.data[0].weather.description
                    },
                    {
                        date: data.data[1].valid_date,
                        max: data.data[1].max_temp,
                        min: data.data[1].min_temp,
                        description: data.data[1].weather.description
                    },
                    {
                        date: data.data[2].valid_date,
                        max: data.data[2].max_temp,
                        min: data.data[2].min_temp,
                        description: data.data[2].weather.description
                    },
                    {
                        date: data.data[3].valid_date,
                        max: data.data[3].max_temp,
                        min: data.data[3].min_temp,
                        description: data.data[3].weather.description
                    },
                    {
                        date: data.data[4].valid_date,
                        max: data.data[4].max_temp,
                        min: data.data[4].min_temp,
                        description: data.data[4].weather.description
                    },
                    {
                        date: data.data[5].valid_date,
                        max: data.data[5].max_temp,
                        min: data.data[5].min_temp,
                        description: data.data[5].weather.description
                    },
                    {
                        date: data.data[6].valid_date,
                        max: data.data[6].max_temp,
                        min: data.data[6].min_temp,
                        description: data.data[6].weather.description
                    }
                ]
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