import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import * as PropTypes from "prop-types";
import Login from "./sites/Login";
import Register from "./sites/Register";
import LastPosts from "./sites/LastPosts";
import Profile from "./sites/Profile";
import NavbarComponent from "./components/Navbar";
import FooterComponent from "./components/Footer";
import Chat from "./sites/Chat";
import VisitProfile from "./sites/VisitProfile";
import Impress from "./sites/Impress";
import Users from "./sites/Users";


function AppRoutes() {
    return (
        <Routes>
            <Route path="/login" element={<Login/>}/>
            <Route path="/register" element={<Register/>}/>
            <Route path="/" element={<LastPosts/>}/>
            <Route path="/profile" element={<Profile/>}/>
            <Route path="/profile/visit" element={<VisitProfile/>}/>
            <Route path="/chat" element={<Chat/>}/>
            <Route path="/users" element={<Users/>}/>
            <Route path="/impress" element={<Impress/>}/>
        </Routes>
    );
}

AppRoutes.propTypes = {children: PropTypes.node};

function App() {
    return (
        <>
            <div >
                <NavbarComponent></NavbarComponent>
                <BrowserRouter>
                    <AppRoutes></AppRoutes>
                </BrowserRouter>
                <FooterComponent></FooterComponent>
            </div>
        </>
    );
}

export default App;

/**
 * <div className="App">
 *       <NavbarComponent/>
 *         <BrowserRouter>
 *             <Routes>
 *                 <Route path="/">
 *                     <Route index element={<Login />} />
 *                     <Route path="register" element={<Register />} />
 *                     <Route path="lastposts" element={<LastPosts />} />
 *                     <Route path="newpost" element={<NewPost />} />
 *                     <Route path="profile" element={<Profile />} />
 *                 </Route>
 *             </Routes>
 *         </BrowserRouter>
 *       <header className="App-header">
 *         <img src={logo} className="App-logo" alt="logo" />
 *         <a
 *           className="App-link"
 *           href="https://reactjs.org"
 *           target="_blank"
 *           rel="noopener noreferrer"
 *         >
 *           Nick ist blöd!
 *           Deresyahr auch
 *         </a>
 *       </header>
 *     </div>
 */
