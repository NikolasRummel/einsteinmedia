import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

import * as authApi from '../api/authApi';
import logo from '../assets/aes.png';
import {Button} from "react-bootstrap";

function NavbarComponent() {

    const user = authApi.getUser();


    return (
        <Navbar expand="lg" sticky="top" variant="dark" style={{backgroundColor: "#1e2124"}}>
            <Container fluid>
                <Navbar.Brand href="#">
                    <img height="40px" src={logo} alt="Logo"/>
                    <b> EinsteinMedia</b>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarScroll"/>
                <Navbar.Collapse id="navbarScroll">
                    <Nav
                        className="me-auto my-2 my-lg-0"
                        style={{maxHeight: '100px'}}
                        navbarScroll
                    >
                        <Nav.Link href="/">Feed</Nav.Link>
                        <Nav.Link href="/profile">My profile</Nav.Link>
                        <Nav.Link href="/chat">General Chat</Nav.Link>
                        <NavDropdown title="Auth" id="navbarScrollingDropdown">
                            <NavDropdown.Item href="/login">Login</NavDropdown.Item>
                            <NavDropdown.Item href="/register">Register</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="ml-auto">
                        {authApi.isLoggedIn() && (
                            <span style={{color: 'white', marginRight: '10px'}}>
                                Logged in as <b>{user.userName}</b>
                            </span>
                        )}
                    </Nav>
                    {authApi.isLoggedIn() && (
                        <Button onClick={authApi.logOut}>Logout</Button>
                    )}
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavbarComponent;
