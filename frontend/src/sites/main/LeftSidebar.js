import React, { useEffect, useState } from "react";
import { Button, Col, Row } from "react-bootstrap";
import { FaBook, FaUser, FaRocket, FaUsers } from "react-icons/fa";
import { NewPost } from "../../components/NewPost";
import Popup from "reactjs-popup";
import * as authApi from "../../api/authApi";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

export default function Leftsidebar() {
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [statistics, setStatistics] = useState({
        users: 0,
        posts: 0,
        newestUser: ""
    });
    const navigate = useNavigate();

    useEffect(() => {
        fetch("http://localhost:8081/statistics")
            .then(response => response.json())
            .then(data => setStatistics(data))
            .catch(error => console.error(error));
    }, []);

    const showCreatePostPopup = () => {
        if (!authApi.isLoggedIn()) {
            Swal.fire({
                icon: "error",
                title: "Not logged in",
                text: "You need to be logged in to create a new post.",
                showConfirmButton: true,
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate("/login");
                }
            });
        } else {
            setIsPopupOpen(true);
        }
    };

    const closeCreatePostPopup = () => {
        setIsPopupOpen(false);
        window.location.reload(false);
    };

    return (
        <div className="card">
            <div className="card-body">
                <div className="text-center mb-4">
                    <h3 className="fw-bold">What is EinsteinMedia?</h3>
                </div>
                <div className="text-muted">
                    <p>
                        EinsteinMedia is a social media platform created as a computer science class project.
                    </p>
                    <p>
                        It provides a space for users to connect and share their thoughts and photos with others who have similar interests.
                    </p>
                </div>
            </div>
            <hr/>
            <div className="card-body">
                <h4>Statistics</h4>
                <Row className="mb-2">
                    <Col xs={1}>
                        <FaUsers size={"20px"} className="sidebar-icon sidebar-active" />
                    </Col>
                    <Col>
                        <span className="text-muted sidebar-link">Users: {statistics.users}</span>
                    </Col>
                </Row>
                <Row className="mb-2">
                    <Col xs={1}>
                        <FaRocket size={"20px"} className="sidebar-icon sidebar-active" />
                    </Col>
                    <Col>
                        <span className="text-muted sidebar-link">Posts: {statistics.posts}</span>
                    </Col>
                </Row>
                <Row className="mb-2">
                    <Col xs={1}>
                        <FaUser size={"20px"} className="sidebar-icon sidebar-active" />
                    </Col>
                    <Col>
                        <span className="text-muted sidebar-link">Newest User: {statistics.newestUser}</span>
                    </Col>
                </Row>
            </div>
            <div className="card-body">
                <Button className="sidebar-button" onClick={showCreatePostPopup}>
                    Create a new post
                </Button>
            </div>
            <Popup open={isPopupOpen} onClose={closeCreatePostPopup}>
                <NewPost onClose={closeCreatePostPopup} />
            </Popup>
        </div>
    );
}
