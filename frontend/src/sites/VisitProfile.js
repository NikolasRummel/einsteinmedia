import React, {useEffect, useState} from 'react';
import {Form, Button, Card} from 'react-bootstrap';
import {useLocation, useNavigate} from "react-router-dom";
import * as authApi from "../api/authApi";
import Feed from "./main/Feed";

function VisitProfile() {
    // Den "message"-Parameter aus der URL abrufen
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const userId = queryParams.get('userId');

    const [email, setEmail] = useState('max@mustermann.de');
    const [firstName, setFirstName] = useState('Max');
    const [lastName, setLastName] = useState('Mustermann');
    const [username, setUsername] = useState('MaxGamer123');
    const [bannerImage, setBannerImage] = useState("https://pbs.twimg.com/profile_banners/44196397/1576183471/600x200");
    const [profileImage, setProfileImage] = useState("https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg");

    const navigate = useNavigate();

    async function fetchUserData() {
        return await authApi.fetchUserById(userId).then(value => {
            setEmail(value.email);
            setFirstName(value.firstName);
            setLastName(value.lastName);
            setUsername(value.userName);
            setProfileImage(value.profileImage);
            setBannerImage(value.bannerImage);
        });
    }

    useEffect(() => {
        fetchUserData().then(r => console.log(r));

    }, []);

    return (
        <div>
            <div className="profile-banner" style={{position: 'relative'}}>
                <img
                    src={bannerImage}
                    alt="Banner"
                />
                <div className="profile-stats">
                    <div className="profile-stat">
                        <h4>Followers</h4>
                        <p>10</p>
                    </div>
                    <div className="profile-stat">
                        <h4>Following</h4>
                        <p>10</p>
                    </div>
                    <div className="profile-stat">
                        <h4>Postings</h4>
                        <p>10</p>
                    </div>
                </div>
                <div className="profile-image-large">
                    <img
                        src={profileImage}
                        alt="Profilbild"
                        className="rounded-circle img-fluid"
                        style={{width: '200px', height: '200px', objectFit: 'cover'}}
                    />
                </div>
            </div>
            <div className="profile-banner">
                <Button className="follow-button">Follow</Button>
            </div>
            <div className="profile-content">
                <div className="container py-5">
                    <div className="row">
                        <div className="col-md-8 col-sm-12">
                            <h3>Profile of {username}</h3>
                            <Card>
                                <Card.Body>
                                    <Card.Text>
                                        <strong>Email adress:</strong> {email}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>Firstname:</strong> {firstName}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>Lastname:</strong> {lastName}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>Username:</strong> {username}
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                        </div>
                        <div className="col-md-4 col-sm-12">
                            <h3>Last posts</h3>
                            <div style={{height: '400px', overflow: 'auto'}}>
                                <Feed></Feed>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default VisitProfile;
