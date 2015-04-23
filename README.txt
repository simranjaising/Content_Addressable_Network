=======================
READ ME
=======================

1. Functionality implemented :
	a) Join
	b) Insert a file
	c) Leave (unto 3 peers)
	d) Search for a given file
	e) View a given peer

STEP 1: Compile all the files in the directory 
	- javac *.java

STEP 2: Run the Server.java Program on one terminal
	- java Server

STEP 3: 1)Now, on another terminal compile the files again
	- javac *.java
	2)Run the Client.java program on the terminal
	- java Client
	
	3)Please enter the Server IP:
	for eg: 129.21.30.50

	4)You will see the following menu:

	Choose one of the following actions: 
	1. Join
	2. Insert a File
	3. Leave
	4. Search
	5. View Peer by IP Address
	6. Exit
	
	5) Now, to make that peer join the network press ‘1’
	The first peer gets set as the bootstrapping IP

	6) Details of the new joined peer are displayed

	7) To make a new node join, run steps 1-6 on another peer (terminal)

STEP 4: To insert a file hit ‘2’
	-Please enter file name if in the same directory, else enter path
	for example: test.txt

	The route taken to reach the destination peer is printed and the destination peer is printed

STEP 5: To make a peer leave the network, hit ‘3’
	
	-Please enter the IP of the peer that you want to remove 
	Enter the IP Address of the peer you want to remove
	
	The neighbors that are affected by this peer leaving print their details on the respective terminals

STEP 6: To search for a file, hit ‘4’
	-Please enter the name of the file you want to search for
	Enter the name of the file and its destination will get printed

STEP 5: To view a peer, hit ‘5’
	-Please enter the IP of the peer that you want to view 
	Enter the IP of the peer you want to view and the details of that peer will get printed on the respective(on that peers) terminal 
	
	