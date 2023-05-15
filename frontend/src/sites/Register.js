import React, {useState} from 'react';
import logo from "../assets/aes.png";
import api from "../api/authapi";
import {Link, Navigate} from "react-router-dom";
import {Alert, Form, Card} from "react-bootstrap";

export default function Register() {

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
            formIsValid = false;
            setemailError("Email Not Valid");
            return false;
        } else {
            setemailError("");
            formIsValid = true;
        }

        return formIsValid;
    };

    const registerSubmit = (e) => {
        e.preventDefault();
        handleValidation();

        fetch(
            "http://localhost:8081/user/register", {
                firstName: firstName,
                lastName: lastName,
                username: userName,
                email: email,
                password: password
            }).then(res => {
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

    function showError(message) {
        return (
            <div className="row mt-3">
                <Alert variant="danger">{message}</Alert>
            </div>
        )
    }

    function redirectToLogin() {
        return (
            <Navigate to='./login'/>
        )
    }

    return (
        <div className="container vh-100 d-flex align-items-center justify-content-center">
            <div className="col-md-5">
                <div className="">
                    <img height="200px" src={logo} alt="Logo"/>
                </div>
                <br/>
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Register</h5>
                        <Form id="registerForm" onSubmit={registerSubmit}>
                            <div className="form-group">
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
                            <div className="form-group">
                                <label>Firstname</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="exampleInputPassword1"
                                    placeholder="Max"
                                    onChange={(event) => setFirstName(event.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Lastname</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="exampleInputPassword1"
                                    placeholder="Musterman"
                                    onChange={(event) => setLastName(event.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Username</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="exampleInputPassword1"
                                    placeholder="CoolGamer123"
                                    onChange={(event) => setUserName(event.target.value)}
                                />
                            </div>
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
                            <button type="submit" className="btn btn-primary">
                                Submit
                            </button>
                            {failAlert && showError("email or username already taken")}
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