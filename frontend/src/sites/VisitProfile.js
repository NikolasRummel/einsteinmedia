import React, {useEffect, useState} from 'react';
import {Form, Button, Card} from 'react-bootstrap';
import {useLocation, useNavigate} from "react-router-dom";
import * as authApi from "../api/authApi";
import Feed from "./main/Feed";
import * as userApi from "../api/userApi";
import PrivateFeed from "./profile/PrivateFeed";
import {sendFollowRequest} from "../api/userApi";

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
    const [followers, setFollowers] = useState();
    const [followees, setFollowees] = useState();
    const [isCurrentUserFollowing, setIsCurrentUserFollowing] = useState(false);

    const navigate = useNavigate();

    const buttonClass = isCurrentUserFollowing ? 'follow-button btn btn-secondary' : 'follow-button btn btn-primary';

    async function checkIfCurrentUserIsFollowingUser(userId) {
        try {
            const isFollowing = await userApi.isCurrentUserFollowingUser(userId, userApi.getUser().uniqueId);

            console.log("isFollowing" + userId + ":" + isFollowing)

            return isFollowing;
        } catch (error) {
            console.error('Fehler beim Überprüfen der Verbindung:', error);
            return false;
        }
    }

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

    const fetchFollowers = async () => {
        try {
            const followersCount = await userApi.fetchFollowersCount(userId)
            setFollowers(followersCount);

            const followeesCount = await userApi.fetchFolloweesCount(userId)
            setFollowees(followeesCount);

            console.log("FOLLOW:")
            console.log(followersCount)
            console.log(followeesCount)
        } catch (error) {
            // Fehlerbehandlung, falls ein Fehler auftritt
            console.log('Fehler bei der Datenabfrage:', error);
            // Hier kannst du entsprechende Fehlerbehandlungsschritte durchführen, z. B. eine Fehlermeldung anzeigen oder den State entsprechend aktualisieren.
        }
    };

    function fetchAllData() {
        fetchUserData()
            .then(() => fetchFollowers())
            .then(() => checkIfCurrentUserIsFollowingUser(userId))
            .then(result => setIsCurrentUserFollowing(result))
            .catch(error => console.error('Fehler:', error));
    }

    function handleFollowButton() {
        if (isCurrentUserFollowing) {
            userApi.sendUnFollowRequest(email)
                .then(() => fetchAllData())
                .catch(error => console.error('Fehler:', error));
        } else {
            userApi.sendFollowRequest(email)
                .then(() => fetchAllData())
                .catch(error => console.error('Fehler:', error));
        }
    }

    function followButtonName (){
        if (!authApi.isLoggedIn()) {
            return "Login to follow "
        } else {
            if(isCurrentUserFollowing) {
                return "unfollow"
            }
            return "follow now"
        }
    }

    useEffect(() => {
        fetchAllData();
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
                        <p>{followers}</p>
                    </div>
                    <div className="profile-stat">
                        <h4>Following</h4>
                        <p>{followees}</p>
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
                <Button className={buttonClass} onClick={handleFollowButton}>{followButtonName()}</Button>
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
                                <PrivateFeed userId={userId}></PrivateFeed>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default VisitProfile;
