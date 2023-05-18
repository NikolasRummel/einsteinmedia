import React, {useState, useEffect} from 'react';
import {Container, Row, Col, Image, Form, Button} from 'react-bootstrap';
import * as authApi from "../api/authApi";
import Swal from "sweetalert2";
import {useNavigate} from "react-router-dom";

const ChatMessage = ({text, author, isOwn, profileImage}) => {
    const messageStyle = isOwn ? "Messages-message currentMember" : "Messages-message";

    return (
        <div>
            <li className={messageStyle}>
                <Image src={profileImage} className="avatar"></Image>
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

    const navigate = useNavigate();

    const user = authApi.getUser();

    useEffect(() => {
        if (!authApi.isLoggedIn()) {
            Swal.fire({
                icon: 'error',
                title: 'Not logged in',
                text: 'You need to be logged in to access this page.',
                showConfirmButton: true,
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate('/login');
                }
            });
        } else { //If logged in

            // Hier wird der WebSocket-Code für den Nachrichtenempfang platziert
            if (!socket) {
                const newSocket = new WebSocket('ws://localhost:8082/chat')
                socket = newSocket;

                newSocket.onopen = (event) => {
                    console.log("opened connection")
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
        }
    }, []);

    const handleSendMessage = () => {
        if (newMessage.trim() !== '') {
            const message = {
                text: newMessage,
                author: user.userName,
                profileImage: user.profileImage,
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

    const handleEnterDown = (event) => {
        if (event.key === 'Enter') {
            handleSendMessage()
        }
    }
    return (
        <div className={!authApi.isLoggedIn() ? "blur-background" : ""}>
            <Container fluid className="col-md-10 vh-100 d-flex flex-column">
                <div className="flex-grow-1 overflow-auto">
                    {messages.map((message, index) => (
                        <div key={index}>
                            <br/>
                            <br/>
                            <ChatMessage
                                text={message.text}
                                author={message.author}
                                profileImage={message.profileImage}
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
                                onKeyDown={handleEnterDown}
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
        </div>
    );
};

export default ChatPage
