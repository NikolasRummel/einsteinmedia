import React, {useEffect, useState} from 'react';
import {Form, Button, Card} from 'react-bootstrap';
import PostCardComponent from "../components/PostCard";
import {useLocation, useNavigate} from "react-router-dom";
import Swal from "sweetalert2";
import * as authApi from "../api/authApi";
import PrivateFeed from "./profile/PrivateFeed";

function Profile() {
    // Den "message"-Parameter aus der URL abrufen
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const message = queryParams.get('message');

    const [email, setEmail] = useState('max@mustermann.de');
    const [firstName, setFirstName] = useState('Max');
    const [lastName, setLastName] = useState('Mustermann');
    const [username, setUsername] = useState('MaxGamer123');
    const [bannerImage, setBannerImage] = useState("https://pbs.twimg.com/profile_banners/44196397/1576183471/600x200");
    const [profileImage, setProfileImage] = useState("https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg");

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

            console.log(user)

            setEmail(user.email);
            setFirstName(user.firstName);
            setLastName(user.lastName);
            setUsername(user.userName);
            setProfileImage(user.profileImage);
            setBannerImage(user.bannerImage);

            if (message === 'success') {
                showSuccessToast()
            }
        }

    }, []);

    const showSuccessToast = () => {
        Swal.fire({
            icon: 'success',
            title: `Logged in as ${authApi.getUser().email}`,
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            background: "#ffffff"
        });
    };

    return (
        <div className={!authApi.isLoggedIn() ? "blur-background" : ""}>
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
            <div className="profile-content">
                <div className="container py-5">
                    <div className="row">
                        <div className="col-md-8 col-sm-12">
                            <h3>My Profile</h3>
                            <Card>
                                <Card.Header>
                                    <Card.Title>Your profile data</Card.Title>
                                </Card.Header>
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
                                <PrivateFeed></PrivateFeed>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default Profile;
