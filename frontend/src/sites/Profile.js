import React, { useState } from 'react';
import { Form, Button, Card } from 'react-bootstrap';
import PostCardComponent from "../components/PostCard";

function Profile() {
    const [name, setName] = useState('Max Mustermann');
    const [email, setEmail] = useState('max.mustermann@example.com');

    const handleNameChange = (event) => {
        setName(event.target.value);
    };

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    return (
        <div>
            <div className="profile-banner" style={{ position: 'relative' }}>
                <img
                    src="https://pbs.twimg.com/profile_banners/44196397/1576183471/600x200"
                    alt="Banner"
                    className="img-fluid"
                />
                <div
                    className="profile-image-large"
                    style={{
                        position: 'absolute',
                        bottom: '-75px',
                        left: '50%',
                        transform: 'translateX(-50%)',
                        borderRadius: '50%',
                        overflow: 'hidden',
                        border: '5px solid #fff',
                        boxShadow: '0 0 5px rgba(0, 0, 0, 0.3)',
                    }}
                >
                    <img
                        src="https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg"
                        alt="Profilbild"
                        className="rounded-circle img-fluid"
                        style={{ width: '200px', height: '200px', objectFit: 'cover' }}
                    />
                </div>
            </div>
            <div className="profile-content">
                <div className="container py-5">
                    <div className="row" style={{heig: ""}}>
                        <div className="col-md-8 col-sm-12">
                            <h3>Mein Profil</h3>
                            <Card>
                                <Card.Header>
                                    <Form>
                                        <Form.Group controlId="formBasicName">
                                            <Form.Label>Name</Form.Label>
                                            <Form.Control
                                                type="text"
                                                placeholder="Name eingeben"
                                                value={name}
                                                onChange={handleNameChange}
                                            />
                                        </Form.Group>

                                        <Form.Group controlId="formBasicEmail">
                                            <Form.Label>E-Mail-Adresse</Form.Label>
                                            <Form.Control
                                                type="email"
                                                placeholder="E-Mail-Adresse eingeben"
                                                value={email}
                                                onChange={handleEmailChange}
                                            />
                                        </Form.Group>

                                        <Button variant="primary" type="submit">
                                            Speichern
                                        </Button>
                                    </Form>
                                </Card.Header>
                            </Card>
                        </div>
                        <div className="col-md-4 col-sm-12">
                            <h3>Letzter Post</h3>
                            <PostCardComponent />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
