import React, {useState, useEffect} from 'react';
import {Container, Row, Col, Image, Form, Button} from 'react-bootstrap';

const ChatMessage = ({text, author, isOwn}) => {
    const messageStyle = isOwn ? "Messages-message currentMember" : "Messages-message";
    const imageSrc = isOwn ? 'https://picsum.photos/50/50' : 'https://picsum.photos/50/50';

    return (
        <div>
            <li className={messageStyle}>
                <Image src={imageSrc} className="avatar"></Image>
                <div className="Message-content">
                    <div className="username">
                        {author}
                    </div>
                    <div className="text">{text}</div>
                </div>
            </li>
        </div>
    );
};

// eslint-disable-next-line react-hooks/rules-of-hooks
let socket = null;

const ChatPage = () => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');


    useEffect(() => {
        // Hier wird der WebSocket-Code für den Nachrichtenempfang platziert
        if (!socket) {
            const newSocket = new WebSocket('ws://localhost:8082/chat')
           socket = newSocket;

            newSocket.onopen = (event) => {
                console.log("now")
                //newSocket.send(JSON.stringify({text: 'Hallo!', author: 'Nikolas', isOwn: false}));
            };

            newSocket.onmessage = (event) => {
                const message = JSON.parse(event.data);
                console.log("new message: " + message)
                setMessages((prevMessages) => [...prevMessages, message]);
                setNewMessage('');
            };

            // WebSocket-Verbindung schließen, wenn die Komponente unmontiert wird
            return () => {
                if (newSocket.readyState === WebSocket.OPEN) {
                    newSocket.close();
                }
            };
        }

    }, []);

    const handleSendMessage = () => {
        if (newMessage.trim() !== '') {
            const message = {
                text: newMessage,
                author: 'Max', // Hier kannst du den Autor der Nachricht festlegen
                isOwn: false,
            };

            // Nachricht über WebSocket senden, wenn die Verbindung besteht
            if (socket && socket.readyState === WebSocket.OPEN) {

                socket.send(JSON.stringify(message));
            }

            message.isOwn = true;

            setMessages([...messages, message]);
            setNewMessage('');
        }
    };


    return (
        <Container fluid className="col-md-10 vh-100 d-flex flex-column">
            <div className="flex-grow-1 overflow-auto">
                {messages.map((message, index) => (
                    <div key={index}>
                        <br/>
                        <br/>
                        <ChatMessage
                            text={message.text}
                            author={message.author}
                            isOwn={message.isOwn}
                        />
                    </div>
                ))}
            </div>

            <div className="Message-input-container">
                <Row className="mt-auto">
                    <Col xs={10}>
                        <Form.Control
                            type="text"
                            placeholder="Neue Nachricht"
                            value={newMessage}
                            onChange={(e) => setNewMessage(e.target.value)}
                        />
                    </Col>
                    <Col xs={2} className="d-flex align-items-center justify-content-end">
                        <Button variant="primary" onClick={handleSendMessage}>
                            Senden
                        </Button>
                    </Col>
                </Row>
            </div>
        </Container>
    );
};

export default ChatPage
