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
    fetch('http://api.weatherbit.io/v2.0/history/hourly?lat='+lat+'&lon='+long+ '&start_date=' + data.data[0].valid_date + "&end_date=" + data.data[1].valid_date +'&key=' + split[3])
    .then(response =>{
        return response.json();
    })
    .then(data =>{
        const payload = {
            "hours" : [
                {
                    temp: data.data[0].temp, 
                    wind_spd: data.data[0].wind_spd,
                    description: data.data[0].weather.description
                },
                {
                    temp: data.data[1].temp, 
                    wind_spd: data.data[1].wind_spd,
                    description: data.data[1].weather.description
                },
                {
                    temp: data.data[2].temp, 
                    wind_spd: data.data[2].wind_spd,
                    description: data.data[2].weather.description
                },
                {
                    temp: data.data[3].temp, 
                    wind_spd: data.data[3].wind_spd,
                    description: data.data[3].weather.description
                },
                {
                    temp: data.data[4].temp, 
                    wind_spd: data.data[4].wind_spd,
                    description: data.data[4].weather.description
                },
                {
                    temp: data.data[5].temp, 
                    wind_spd: data.data[5].wind_spd,
                    description: data.data[5].weather.description
                },
                {
                    temp: data.data[6].temp, 
                    wind_spd: data.data[6].wind_spd,
                    description: data.data[6].weather.description
                },
                {
                    temp: data.data[7].temp, 
                    wind_spd: data.data[7].wind_spd,
                    description: data.data[7].weather.description
                },
                {
                    temp: data.data[8].temp, 
                    wind_spd: data.data[8].wind_spd,
                    description: data.data[8].weather.description
                },
                {
                    temp: data.data[9].temp, 
                    wind_spd: data.data[9].wind_spd,
                    description: data.data[9].weather.description
                },
                {
                    temp: data.data[10].temp, 
                    wind_spd: data.data[10].wind_spd,
                    description: data.data[10].weather.description
                },
                {
                    temp: data.data[11].temp, 
                    wind_spd: data.data[11].wind_spd,
                    description: data.data[11].weather.description
                },
                {
                    temp: data.data[12].temp, 
                    wind_spd: data.data[12].wind_spd,
                    description: data.data[12].weather.description
                },
                {
                    temp: data.data[13].temp, 
                    wind_spd: data.data[13].wind_spd,
                    description: data.data[13].weather.description
                },
                {
                    temp: data.data[14].temp, 
                    wind_spd: data.data[14].wind_spd,
                    description: data.data[14].weather.description
                },
                {
                    temp: data.data[15].temp, 
                    wind_spd: data.data[15].wind_spd,
                    description: data.data[15].weather.description
                },
                {
                    temp: data.data[16].temp, 
                    wind_spd: data.data[16].wind_spd,
                    description: data.data[16].weather.description
                },
                {
                    temp: data.data[17].temp, 
                    wind_spd: data.data[17].wind_spd,
                    description: data.data[17].weather.description
                },
                {
                    temp: data.data[18].temp, 
                    wind_spd: data.data[18].wind_spd,
                    description: data.data[18].weather.description
                },
                {
                    temp: data.data[19].temp, 
                    wind_spd: data.data[19].wind_spd,
                    description: data.data[19].weather.description
                },
                {
                    temp: data.data[20].temp, 
                    wind_spd: data.data[20].wind_spd,
                    description: data.data[20].weather.description
                },
                {
                    temp: data.data[21].temp, 
                    wind_spd: data.data[21].wind_spd,
                    description: data.data[21].weather.description
                },
                {
                    temp: data.data[22].temp, 
                    wind_spd: data.data[22].wind_spd,
                    description: data.data[22].weather.description
                },
                {
                    temp: data.data[23].temp, 
                    wind_spd: data.data[23].wind_spd,
                    description: data.data[23].weather.description
                },
                
            ]
        }

        response.send({
            //req.query is a reference to arguments a
            message: payload
        })
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
    fetch('http://api.weatherbit.io/v2.0/history/hourly?lat='+'&postal_code='+zip+ '&start_date=' + data.data[0].valid_date + "&end_date=" + data.data[1].valid_date +'&key=' + split[3])
    .then(response =>{
        return response.json();
    })
    .then(data =>{
        const payload = {
            "hours" : [
                {
                    temp: data.data[0].temp, 
                    wind_spd: data.data[0].wind_spd,
                    description: data.data[0].weather.description
                },
                {
                    temp: data.data[1].temp, 
                    wind_spd: data.data[1].wind_spd,
                    description: data.data[1].weather.description
                },
                {
                    temp: data.data[2].temp, 
                    wind_spd: data.data[2].wind_spd,
                    description: data.data[2].weather.description
                },
                {
                    temp: data.data[3].temp, 
                    wind_spd: data.data[3].wind_spd,
                    description: data.data[3].weather.description
                },
                {
                    temp: data.data[4].temp, 
                    wind_spd: data.data[4].wind_spd,
                    description: data.data[4].weather.description
                },
                {
                    temp: data.data[5].temp, 
                    wind_spd: data.data[5].wind_spd,
                    description: data.data[5].weather.description
                },
                {
                    temp: data.data[6].temp, 
                    wind_spd: data.data[6].wind_spd,
                    description: data.data[6].weather.description
                },
                {
                    temp: data.data[7].temp, 
                    wind_spd: data.data[7].wind_spd,
                    description: data.data[7].weather.description
                },
                {
                    temp: data.data[8].temp, 
                    wind_spd: data.data[8].wind_spd,
                    description: data.data[8].weather.description
                },
                {
                    temp: data.data[9].temp, 
                    wind_spd: data.data[9].wind_spd,
                    description: data.data[9].weather.description
                },
                {
                    temp: data.data[10].temp, 
                    wind_spd: data.data[10].wind_spd,
                    description: data.data[10].weather.description
                },
                {
                    temp: data.data[11].temp, 
                    wind_spd: data.data[11].wind_spd,
                    description: data.data[11].weather.description
                },
                {
                    temp: data.data[12].temp, 
                    wind_spd: data.data[12].wind_spd,
                    description: data.data[12].weather.description
                },
                {
                    temp: data.data[13].temp, 
                    wind_spd: data.data[13].wind_spd,
                    description: data.data[13].weather.description
                },
                {
                    temp: data.data[14].temp, 
                    wind_spd: data.data[14].wind_spd,
                    description: data.data[14].weather.description
                },
                {
                    temp: data.data[15].temp, 
                    wind_spd: data.data[15].wind_spd,
                    description: data.data[15].weather.description
                },
                {
                    temp: data.data[16].temp, 
                    wind_spd: data.data[16].wind_spd,
                    description: data.data[16].weather.description
                },
                {
                    temp: data.data[17].temp, 
                    wind_spd: data.data[17].wind_spd,
                    description: data.data[17].weather.description
                },
                {
                    temp: data.data[18].temp, 
                    wind_spd: data.data[18].wind_spd,
                    description: data.data[18].weather.description
                },
                {
                    temp: data.data[19].temp, 
                    wind_spd: data.data[19].wind_spd,
                    description: data.data[19].weather.description
                },
                {
                    temp: data.data[20].temp, 
                    wind_spd: data.data[20].wind_spd,
                    description: data.data[20].weather.description
                },
                {
                    temp: data.data[21].temp, 
                    wind_spd: data.data[21].wind_spd,
                    description: data.data[21].weather.description
                },
                {
                    temp: data.data[22].temp, 
                    wind_spd: data.data[22].wind_spd,
                    description: data.data[22].weather.description
                },
                {
                    temp: data.data[23].temp, 
                    wind_spd: data.data[23].wind_spd,
                    description: data.data[23].weather.description
                },
                
            ]
        }

        response.send({
            //req.query is a reference to arguments a
            message: payload
        })
})
})
})
    }
})




module.exports = router 