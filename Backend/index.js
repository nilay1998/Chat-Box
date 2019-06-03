const express=require('express');
const socket=require('socket.io');
const app=express();

const port = process.env.PORT || 3000;
const server=app.listen(port, () => console.log(`Listening on port ${port}...`));

const io=socket(server);

app.get('/', async(req,res)=>{
    console.log('GET REQ');
    res.send('Hello');
});

io.on('connection', function(socket){
    console.log('MADE SOCKET CONNECTION');

    socket.on('join', userNickname => {
        console.log(userNickname +" : has joined the chat");
        socket.broadcast.emit('userjoinedthechat',userNickname +" : has joined the chat");
    });

    socket.on('messagedetection', (senderNickname, messageContent) => {
        console.log(senderNickname+" : " +messageContent)
        const message = { 'message':messageContent,'senderNickname':senderNickname};
        io.emit('message',message);
    });

    socket.on('kill', abc => {
        console.log(abc);
        socket.broadcast.emit( "userdisconnect" ,abc + " : user has left");
    });
});

// const express = require('express'),
// http = require('http'),
// app = express(),
// server = http.createServer(app),
// io = require('socket.io').listen(server);
// app.get('/', (req, res) => {
// res.send('Chat Server is running on port 3000')
// });
// io.on('connection', (socket) => {

// console.log('user connected')
// socket.on('join', function(userNickname) {
//         console.log(userNickname +" : has joined the chat "  );
//         socket.broadcast.emit('userjoinedthechat',userNickname +" : has joined the chat ");
//     })

// socket.on('messagedetection', (senderNickname,messageContent) => {
//        //log the message in console 
//        console.log(senderNickname+" : " +messageContent)
//       //create a message object 
//       let  message = {"message":messageContent, "senderNickname":senderNickname}
//        // send the message to all users including the sender  using io.emit() 
//       io.emit('message', message )
//       })

// socket.on('disconnect', function() {
//         console.log(userNickname +' has left ')
//         socket.broadcast.emit( "userdisconnect" ,' user has left')
//     })
// })
// server.listen(3000,()=>{
// console.log('Node app is running on port 3000')
// })
