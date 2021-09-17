const http = require('http');

const host = 'localhost';
const port = 8000;

const dish = {id: 'eb646a86-c948-4b1f-b275-18b9880646eb', type: 'Paper Plates', style: 'Colored Glass Plates'};

const requestListener = function (req, res) {
    switch (req.url) {
        case '/order/dish':
            res.writeHead(200, {'Content-Type': 'application/json'});
            res.end(JSON.stringify(dish));
            break;
        default:
            res.end();
            break;
    }
};

const server = http.createServer(requestListener);
server.listen(port, host, () => {
    console.log(`Server is running on http://${host}:${port}`);
});
