import React, {useEffect, useState} from 'react';
import {Card} from "react-bootstrap";
import {Link, useLocation} from "react-router-dom";
import Swal from "sweetalert2";

export default function Login() {

    // Die "message"-Parameter aus der URL abrufen
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const message = queryParams.get('message');

    // Login vars
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [passwordError, setpasswordError] = useState("");
    const [emailError, setemailError] = useState("");

    const [failAlert, setFailAlert] = useState(false);
    const [redirect, setRedirect] = useState(false);

    useEffect(() => {
        if (message === 'success') {
            showSuccessToast()
        }
    }, [message]);

    const showSuccessToast = () => {
        Swal.fire({
            icon: 'success',
            title: 'Successfully created account',
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 5000,
            timerProgressBar: true,
            background: "#ffffff"
        });
    };

    const handleValidation = (event) => {
        let formIsValid = true;

        if (!email.match(/^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/)) {
            formIsValid = false;
            setemailError("Email Not Valid");
            return false;
        } else {
            setemailError("");
            formIsValid = true;
        }

        return formIsValid;
    };

    const loginSubmit = (e) => {
        e.preventDefault();
        handleValidation();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/plain' },
            body: JSON.stringify({
                email: email,
                password: password
            })
        };

        fetch("http://localhost:8081/user/login",  requestOptions)
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

    return (
        <div className="container vh-100 d-flex align-items-center justify-content-center">
            <div className="card col-md-5">
                <div className="card-body">
                    <div className="d-flex justify-content-center align-items-center mt-4">
                        <h2 className="card-title">Login</h2>
                    </div>
                    <form id="loginform" onSubmit={loginSubmit}>
                        <div className="form-group">
                            <br/>
                            <label>Email address</label>
                            <input
                                type="email"
                                className="form-control"
                                id="EmailInput"
                                name="EmailInput"
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
                            <label>Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="exampleInputPassword1"
                                placeholder="Password"
                                onChange={(event) => setPassword(event.target.value)}
                            />
                            <small id="passworderror" className="text-danger form-text">
                                {passwordError}
                            </small>
                        </div>
                        <div className="d-flex justify-content-center align-items-center mt-4">
                            <button type="submit" className="btn btn-primary align-self-center">
                                Submit
                            </button>
                        </div>
                    </form>
                    <div className="d-flex justify-content-center align-items-center mt-4">
                                      <span className="fw-normal">
                                        Dont have an account?
                                          {/* eslint-disable-next-line react/jsx-no-undef */}
                                          <Card.Link as={Link} to="/register/" className="fw-bold">
                                          {` Create one here `}
                                        </Card.Link>
                                      </span>
                    </div>
                </div>
            </div>
        </div>
    );
}