const express = require('express');
const router = express.Router();
const axios = require('axios');
const fs = require('fs');

router.post('/', function (req, res) {
    fs.readFile(__dirname + '/blueprint.json', (err, blueprint) => {
        if (err) throw err;

        const buffer = new Buffer(blueprint);
        const encodedBlueprint = buffer.toString('base64');

        axios.post('http://localhost:8080/scenarios', {
            blueprint: encodedBlueprint,
            schedulingType: "INDEFINITE",
            schedulingInterval: 900,
            folderId: 22143,
            teamId: 55228
        }).then(response => {
            console.log(`Status code: ${response.status}`);
            console.log(response.data);
            res.json(response.data);
        }).catch(err => {
            console.error(err);
            res.render("");
        });
    });
});

router.get('/:scenarioId/blueprint', function (req, res) {
    const scenarioId = req.params.scenarioId;
    axios.get(`http://localhost:8080/scenarios/${scenarioId}/blueprint`)
        .then(response => {
            console.log(`Status code: ${response.status}`);
            console.log(response.data);
            res.json(response.data);
        }).catch(err => {
            console.error(err);
            res.render("");
        });
});
module.exports = router;
