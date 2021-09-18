const http = require('http');

const host = 'localhost';
const port = 8000;


class DishMachine {
    constructor() {
        this.plateTypes = ['Ceramic', 'Glass Plates', 'Melamine', 'Stoneware Plates', 'Earthenware Plates', 'Bamboo Plates', 'Paper Plates', 'Disposable Plastic Plates'];
        this.plateStyles = ['Clear Glass Plates', 'Colored Glass Plates', 'Bone China', 'Fine China', 'Traditional Stoneware Plates', 'Creamware']
    }
    createDish() {
        return {id: this.uuidV4(), type: this.types(), style: this.styles()};
    }
    uuidV4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    types() {
        return this.random(this.plateTypes);
    }
    styles() {
        return this.random(this.plateStyles);
    }
    random(array) {
        return array[Math.floor(Math.random() * array.length)];
    }
}

const machine = new DishMachine();

const requestListener = function (req, res) {
    switch (req.url) {
        case '/order/dish':
            const dish = machine.createDish();
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
