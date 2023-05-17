import React, {useEffect, useState} from "react";
import {Button} from "react-bootstrap";
import {FaAccusoft, FaClock, FaDiscord, FaInstagram, FaThumbsUp, FaTiktok, FaYoutube} from "react-icons/fa";
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
    };

    return (
        <>

            <div className="card">
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">
                        <div className="h5">Heading 1</div>
                        <div className="h5 text-muted sidebar-link">
                            <FaAccusoft size={"20px"} className="sidebar-icon"/> Test
                        </div>
                    </li>
                    <li className="list-group-item">
                        <div className="h5">Heading 2</div>
                        <div className="h5 text-muted sidebar-link">
                            <FaDiscord size={"20px"} className="sidebar-icon"/> Hi
                        </div>
                        <div className="h5 text-muted sidebar-link">
                            <FaClock size={"20px"} className="sidebar-icon"/> Hi
                        </div>
                        <div className="h5 text-muted sidebar-link">
                            <FaYoutube size={"20px"} className="sidebar-icon"/> Hi
                        </div>
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
