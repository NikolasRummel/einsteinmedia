import React, {useEffect, useState} from 'react';
import * as userApi from "../api/userApi";
import LeftSidebar from './main/LeftSidebar';
import RightSidebar from './main/RightSidebar';
import {Button, Card, Col, Container, Form, Row} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

export default function Users() {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        async function fetchUsers() {
            try {
                const fetchedUsers = await userApi.fetchUsers();
                setUsers(fetchedUsers);
            } catch (error) {
                console.log("Error fetching users:", error);
            }
        }

        fetchUsers();
    }, []);

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredUsers = users.filter((user) =>
        user.userName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.lastName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div>
            <br/>
            <div className="container-fluid" style={{minHeight: '80vh', display: 'flex', flexDirection: 'column'}}>
                <div className="row flex-grow-1">
                    <div className="col-md-9">
                        <UsersList filteredUsers={filteredUsers} searchTerm={searchTerm} handleSearch={handleSearch}/>
                    </div>
                    <div className="col-md-3 sticky-sidebar">
                        <RightSidebar/>
                    </div>
                </div>
            </div>
        </div>
    );
}

const UsersList = ({filteredUsers, searchTerm, handleSearch}) => (
    <Container className="d-flex flex-column vh-100">
        <Card className="p-4">
            <h2>User Search</h2>
            <p>{filteredUsers.length} user(s) matching your query</p>

            <div className="w-100 align-items-center">
                <Form.Control
                    className="mb-3 w-70 text-white"
                    type="text"
                    placeholder="Search by username, firstName, or lastName"
                    value={searchTerm}
                    onChange={handleSearch}
                    style={{backgroundColor: "#36393EFF"}}
                />

                <Row>
                    {filteredUsers.slice(-10).map((user, index) => (
                        <Col key={index} xs={6} className="mb-4">
                            <UserCard user={user}/>
                        </Col>
                    ))}
                </Row>
            </div>
        </Card>
    </Container>
);
const UserCard = ({ user }) => {
    const navigate = useNavigate();

    const handleVisitProfile = () => {
        const userId = user.uniqueId;
        navigate(`/profile/visit?userId=${userId}`);
    };

    return (
        <div className="d-flex align-items-center opacity-70 user-card">
            <Card text="black" className="rounded" style={{ width: "100%" }}>
                <Card.Body className="d-flex">
                    <div className="flex-grow-1">
                        <div className="d-flex align-items-center">
                            <div className="rounded-xl overflow-hidden bg-tertiary aspect-square user-card-image">
                                <img
                                    src={user.profileImage}
                                    alt={user.userName}
                                    className="object-center object-cover w-100 h-100"
                                />
                            </div>
                            <div className="p-lg-3">
                                <div className="space-y-1">
                                    <Card.Title className="line-clamp-2 font-weight-bold text-truncate">
                                        {user.userName}
                                    </Card.Title>
                                    <Card.Text className="text-secondary line-clamp-2 text-truncate">
                                        {user.firstName} {user.lastName}
                                    </Card.Text>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="ml-auto d-flex align-items-center"> {/* Add align-items-center class */}
                        <Button variant="primary" onClick={handleVisitProfile}>
                            Visit Profile
                        </Button>
                    </div>
                </Card.Body>
            </Card>
        </div>
    );
};
