import React, {useEffect, useState} from "react";
import {Button} from "react-bootstrap";
import {
    FaBook,
    FaGamepad,
    FaRocket,
} from "react-icons/fa";
import {NewPost} from "../../components/NewPost";
import Popup from "reactjs-popup";
import * as authApi from "../../api/authApi";
import Swal from "sweetalert2";
import {useNavigate} from "react-router-dom";

export default function Leftsidebar() {

    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const navigate = useNavigate();


    const showCreatePostPopup = () => {
        if (!authApi.isLoggedIn()) {
            Swal.fire({
                icon: 'error',
                title: 'Not logged in',
                text: 'You need to be logged in to create a new post.',
                showConfirmButton: true,
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate('/login');
                }
            });
        } else { //If logged in
            setIsPopupOpen(true);
        }

    };

    const closeCreatePostPopup = () => {
        setIsPopupOpen(false);

        window.location.reload(false);
    };

    return (
        <>
            <div className="card">
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">
                        <div className="text-center">
                            <div className="h3">What is EinsteinMedia?</div>
                        </div>
                        <div className="h7 text-muted">
                            EinsteinMedia is a social media platform created as a computer science class project.
                            <br/>
                            <br/>
                            It provides a space for users to connect and share their thoughts and photos with others who
                            have similar interests.
                        </div>
                    </li>
                    <li className="list-group-item">
                        <div className="h4">Posting categories</div>
                        <a href={"/"}>
                            <div className="h5 text-muted sidebar-link">
                                <FaBook size={"20px"} className="sidebar-icon sidebar-active"/> General
                            </div>
                        </a>
                        <a href={"/"}>
                            <div className="h5 text-muted sidebar-link">
                                <FaRocket size={"20px"} className="sidebar-icon"/> Science
                            </div>
                        </a>
                        <a href={"/"}>
                            <div className="h5 text-muted sidebar-link">
                                <FaGamepad size={"20px"} className="sidebar-icon"/> Other
                            </div>
                        </a>
                    </li>
                    <Button className="sidebar-button" onClick={showCreatePostPopup}>Create a new post</Button>

                    <Popup open={isPopupOpen} onClose={closeCreatePostPopup}>
                        <NewPost onClose={closeCreatePostPopup}/>
                    </Popup>
                </ul>
            </div>
        </>
    );
}
