INSERT INTO m_user(username, email, password, name, bio, created_by) VALUES 
('dummyUsername1', 'dummyEmail1@gmail.com', 'dummyPassword1', 'dummyName1', 'dummyBio1', 0),
('dummyUsername2', 'dummyEmail2@gmail.com', 'dummyPassword2', 'dummyName2', 'dummyBio2', 0),
('dummyUsername3', 'dummyEmail3@gmail.com', 'dummyPassword3', 'dummyName3', 'dummyBio3', 0),
('dummyUsername4', 'dummyEmail4@gmail.com', 'dummyPassword4', 'dummyName4', 'dummyBio4', 0),
('dummyUsername8', 'dummyEmail8@gmail.com', '$2a$10$CJW/jdWdVNmDTXE53NDUM.z5PS/NFrZO4WW3mZhaMFy5pXb4f0YBO', 'dummyName8', 'dummyBio8', 0);

INSERT INTO follows(id_follower, id_following, created_by) VALUES 
(1,2,1),
(3,1,1),
(2,1,2);