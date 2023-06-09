import React, {useEffect, useState} from "react";
import PostCard from "../../components/PostCard";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {FaArrowUp} from "react-icons/fa";


const Feed = () => {
    const [posts, setPosts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetch("http://localhost:8081/posts")
            .then((response) => response.json())
            .then((data) => {
                setPosts(data);
                console.log(data + "!!")
            })
            .catch((error) => console.log(error));
    }, []);

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredPosts = posts.filter((post) =>
        post.post.headline.toLowerCase().includes(searchTerm.toLowerCase()) ||
        post.post.text.toLowerCase().includes(searchTerm.toLowerCase()) ||
        post.author.userName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        post.author.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        post.author.lastName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth',
        })
    }

    return (
        <>
            {posts.length === 0 ? (
                <Card className="text-center">
                    <Card.Body>
                        <Card.Title>
                            Hey, there are no posts yet!
                        </Card.Title>
                        <Card.Text>
                            Register or login to create the first post :)
                        </Card.Text>
                    </Card.Body>
                </Card>
            ) : (
                <div>
                    <Card className="p-2 mb-5">
                        <h4>Search a post</h4>

                        <div className="w-100 align-items-center">
                            <Form.Control
                                className="mb-3 w-70 text-white"
                                type="text"
                                placeholder="Search by author or post"
                                value={searchTerm}
                                onChange={handleSearch}
                                style={{backgroundColor: "#36393EFF"}}
                            />
                        </div>
                    </Card>

                    {filteredPosts.map((item, index) => (
                        <div key={index}>
                            <PostCard
                                profileImage={item.author.profileImage}
                                userName={item.author.userName}
                                firstName={item.author.firstName}
                                lastName={item.author.lastName}
                                timestamp={item.post.timestamp}
                                headline={item.post.headline}
                                text={item.post.text}
                                postUniqueId={item.post.uniqueId}
                                userUniqueId={item.author.uniqueId}
                                imageLink={item.post.imageLink}
                            />
                        </div>
                    ))}

                    {(filteredPosts.length >= 4) && (
                        <div onClick={scrollToTop} className="text-center m-4">
                            <Button className="scroll-to-top">
                                <FaArrowUp className="icon"/>
                                <span className="text"> Scroll to Top</span>
                            </Button>
                        </div>
                    )}
                </div>
            )}
        </>
    );
}

export default Feed;
