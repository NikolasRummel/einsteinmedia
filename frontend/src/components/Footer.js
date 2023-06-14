import React from 'react';
import {
    MDBFooter,
    MDBContainer,
    MDBCol,
    MDBRow,
} from 'mdb-react-ui-kit';

function FooterComponent() {
    return (
        <MDBFooter bgColor='dark' className='text-white text-center text-lg-left' style={{ bottom: '0', width: '100%'}}>
            <MDBContainer className='p-4'>
                <MDBRow>
                    <MDBCol lg='6' md='12' className='mb-4 mb-md-0'>
                        <h5 className='text-uppercase'>EinsteinMedia</h5>

                        <p>
                            EinsteinMedia is a social media platform providing space for users to connect and share their thoughts and photos with others who have similar interests.
                        </p>
                    </MDBCol>

                    <MDBCol lg='3' md='6' className='mb-4 mb-md-0'>
                        <h5 className='text-uppercase'>Links</h5>

                        <ul className='list-unstyled mb-0'>
                            <li>
                                <a href='/impress' className='text-white'>
                                    Impress
                                </a>
                            </li>
                        </ul>
                    </MDBCol>

                    <MDBCol lg='3' md='6' className='mb-4 mb-md-0'>
                        <h5 className='text-uppercase mb-0'>Links</h5>

                        <ul className='list-unstyled'>
                            <li>
                                <a href='/privacy' className='text-white'>
                                    Privacy
                                </a>
                            </li>
                        </ul>
                    </MDBCol>
                </MDBRow>
            </MDBContainer>

            <div className='text-center p-3' style={{backgroundColor: 'rgba(0, 0, 0, 0.2)'}}>
                &copy; {new Date().getFullYear()} Copyright:{' '}
                <a className='text-white' href='https://nikolas-rummel.de/'>
                    Nikolas Rummel
                </a>
            </div>
        </MDBFooter>
    );
}

export default FooterComponent;