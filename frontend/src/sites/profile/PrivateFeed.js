import React, {useEffect, useState} from "react";
import PostCard from "../../components/PostCard";
import {Card} from "react-bootstrap";
import * as authApi from "../../api/authApi";

const PrivateFeed = ({userId}) => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: {
                'Authorization': authApi.getAuthKey(),
            },
        };

        fetch("http://localhost:8081/posts/user/" + userId, requestOptions)
            .then((response) => response.json())
            .then((data) => {
                setPosts(data);
            })
            .catch((error) => console.log(error));
    }, []);

    return (
        <>
            {posts.length === 0 ? (
                <Card className="text-center">
                    <Card.Body>
                        <Card.Title>
                            Hey, you don't have any posts yet!
                        </Card.Title>
                        <Card.Text>
                            Start sharing your thoughts, experiences, and interests with others by creating your first post!
                        </Card.Text>
                    </Card.Body>

                </Card>
            ) : (
                posts.map((item, index) => (
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
                        />
                    </div>
                ))
            )}

        </>
    );
}

export default PrivateFeed;
