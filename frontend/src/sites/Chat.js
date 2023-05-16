import React, {useState} from 'react';
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

const ChatPage = () => {
    const [messages, setMessages] = useState([
        {text: 'Hallo!', author: 'Nikolas', isOwn: true},
        {text: 'Hi!', author: 'Anna', isOwn: false},
        {text: 'Servus, wie gehts euch?', author: 'Nils', isOwn: false},
        {text: 'Gut und selbst?', author: 'Nikolas', isOwn: true},
        {text: 'Auch super :)', author: 'Nils', isOwn: false},
    ]);
    const [newMessage, setNewMessage] = useState('');

    const handleSendMessage = () => {
        if (newMessage.trim() !== '') {
            const message = {
                text: newMessage,
                author: 'Max', // Hier kannst du den Autor der Nachricht festlegen
                isOwn: true,
            };
            setMessages([...messages, message]);
            setNewMessage('');
        }
    };

    return (
        <Container fluid className="col-md-10 vh-100 d-flex flex-column">
            <div className="flex-grow-1 overflow-auto">
                {messages.map((message, index) => (
                    <div key={index}>
                        <br />
                        <br />
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

export default ChatPage;