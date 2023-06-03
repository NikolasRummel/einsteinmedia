import React, {useEffect, useState} from "react";
import {Card} from "react-bootstrap";
import * as authApi from "../../api/authApi";
import * as userApi from "../../api/userApi";

export default function Rightsidebar() {

    const user = authApi.getUser();

    const [followees, setFollowees] = useState([]);

    const renderFollowees = () => {
        if (followees.length === 0) {
            return <p>Visit a profile to make contacts</p>;
        } else {
            return followees.map((followee) => (
                <div key={followee.id} style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                    <img height="40px" src={followee.profileImage} alt="UserProfile" className="profile-image" />
                    <Card.Text style={{ marginLeft: '5px' }}>{followee.userName}</Card.Text>
                </div>
            ));
        }
    }


    useEffect(() => {
        const fetchData = async () => {
            try {
                return await userApi.fetchFollowees(user.uniqueId);
            } catch (error) {
                console.error('Error fetching followees:', error);
            }
        };

        fetchData().then(value => {
            console.log(value)
            setFollowees(value)
        });
    }, []);

    return (
        <>
            {!authApi.isLoggedIn() && (
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">New at Einsteinmedia?</h5>
                        <h6 className="card-subtitle mb-2 text-muted">Create a new account or log in </h6>
                        <a href="/register" className="card-link">
                            Create new account
                        </a>
                        <a href="/login" className="card-link">
                            Login to my account
                        </a>
                    </div>
                </div>
            )}
            {
                authApi.isLoggedIn() && (
                    <Card>
                        <Card.Body>
                            <Card.Title><b>Contacts</b></Card.Title>
                            {renderFollowees()}
                        </Card.Body>
                    </Card>
                )
            }
            <br/>
            <iframe src="https://discord.com/widget?id=615119175127400458&theme=dark" width="410" height="750"
                    frameBorder="0"
                    sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>
        </>
    );
}
