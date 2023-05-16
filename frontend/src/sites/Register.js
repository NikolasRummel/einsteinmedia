import React, {useEffect, useState} from 'react';
import {Link, Navigate} from "react-router-dom";
import {Form, Card} from "react-bootstrap";
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'

export default function Register() {

    const SweetAlert = withReactContent(Swal)
    const [bannerImage, setBannerImage] = useState("https://pbs.twimg.com/profile_banners/44196397/1576183471/600x200");
    const [profileImage, setProfileImage] = useState("https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [passwordError, setpasswordError] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [userName, setUserName] = useState("");
    const [emailError, setemailError] = useState("");

    const [failAlert, setFailAlert] = useState(false);
    const [redirect, setRedirect] = useState(false);

    const handleValidation = (event) => {
        let formIsValid = true;

        if (!email.match(/^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/)) {
            setemailError("Email Not Valid");
            return false;
        } else {
            setemailError("");
            formIsValid = true;
        }

        return formIsValid;
    };

    const showErrorModal = () => {
        setFailAlert(false)
        Swal.fire({
            icon: 'error',
            title: 'Oups!',
            text: 'Email or username already taken',
        }).then(r => console.log(r));
    };

    const registerSubmit = (e) => {
        e.preventDefault();
        handleValidation();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/plain' },
            body: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                username: userName,
                email: email,
                password: password,
                profileImage: profileImage,
                bannerImage: bannerImage,
            })
        };

        fetch("http://localhost:8081/user/register",  requestOptions)
            .then(res => {
                console.log(res)
            if (res.status === 200) {
                //Wenn Anfrage Erfolgreich
                //Weiterleitung an Login-Page, kein Error Feld
                setFailAlert(false)
                setRedirect(true)
            } else {
                //Anfrage nicht Erfolgreich
                //Keine Weiterleitung an Login-Page, Error Feld anzeigen
                setFailAlert(true)
                setRedirect(false)
            }
        }).catch(ex => {
            //Bei geworfener Exception
            //Keine Weiterleitung an Login-Page, Error Feld anzeigen
            setFailAlert(true)
            setRedirect(false)
        })
    };

    function redirectToLogin() {
        return (
            <Navigate to={{ pathname: '/login/', search: '?message=success' }} />
        )
    }

    return (
        <div className="container vh-100 d-flex align-items-center justify-content-center">
            <div className="col-md-5">
                <br/>
                <div className="card">
                    <div className="card-body">
                        <div className="d-flex justify-content-center align-items-center mt-4">
                            <h2 className="card-title">Register</h2>
                        </div>
                        <Form id="registerForm" onSubmit={registerSubmit}>
                            <br/>
                            <div className="form-group">
                                <label>Email address</label>
                                <input
                                    type="email"
                                    className="form-control"
                                    id="EmailInput"
                                    name="EmailInput"
                                    required="true"
                                    aria-describedby="emailHelp"
                                    placeholder="Enter email"
                                    onChange={(event) => setEmail(event.target.value)}
                                />
                                <small id="emailHelp" className="text-danger form-text">
                                    {emailError}
                                </small>
                            </div>
                            <br/>
                            <div className="form-group">
                                <label>Username</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="CoolGamer123"
                                    required="true"
                                    onChange={(event) => setUserName(event.target.value)}
                                />
                            </div>
                            <br/>
                            <div className="form-group d-flex">
                                <div className="flex-fill" style={{ marginRight: '10px' }}>
                                    <label>Firstname</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Max"
                                        required="true"
                                        onChange={(event) => setFirstName(event.target.value)}
                                    />
                                </div>
                                <div className="flex-fill">
                                    <label>Lastname</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Musterman"
                                        required="true"
                                        onChange={(event) => setLastName(event.target.value)}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="form-group d-flex">
                                <div className="flex-fill" style={{ marginRight: '10px' }}>
                                    <label>Profile Image Link</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="https://image.png"
                                        onChange={(event) => setProfileImage(event.target.value)}
                                    />
                                </div>
                                <div className="flex-fill ml-2">
                                    <label>Profile Banner Link</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="https://image.png"
                                        onChange={(event) => setBannerImage(event.target.value)}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="form-group">
                                <label>Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    placeholder="Password"
                                    required="true"
                                    onChange={(event) => setPassword(event.target.value)}
                                />
                                <small id="passworderror" className="text-danger form-text">
                                    {passwordError}
                                </small>
                            </div>
                            <div className="d-flex justify-content-center align-items-center mt-4">
                                <button type="submit" className="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                            {failAlert && showErrorModal()}
                            {redirect && redirectToLogin()}
                        </Form>

                        <div className="d-flex justify-content-center align-items-center mt-4">
                                      <span className="fw-normal">
                                        Already have an account?
                                          {/* eslint-disable-next-line react/jsx-no-undef */}
                                          <Card.Link as={Link} to="/login/" className="fw-bold">
                                          {` Login here `}
                                        </Card.Link>
                                      </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}