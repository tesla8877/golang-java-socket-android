package echoserver

import (
	"fmt"
	"net"
)

//Export EchoSocketServer
func EchoSocketServer() {
	// creates server socket
    serverSocket, err := net.Listen("tcp", ":8080")
    if err != nil {
        fmt.Println("Failed to create socket")
        return
    }

    defer serverSocket.Close()

    for {
        // accepts incoming connections
        clientSocket, err := serverSocket.Accept()
        if err != nil {
            fmt.Println("Failed to accept incoming connection.")
            continue
        }

        go handleClient(clientSocket)
    }
}

func handleClient(clientSocket net.Conn) {
    defer clientSocket.Close()

    // echos client's messages
    buffer := make([]byte, 1024)
    for {
        bytesRead, err := clientSocket.Read(buffer)
        if err != nil {
            fmt.Println("Error reading from client:", err)
            break
        }

        _, err = clientSocket.Write(buffer[:bytesRead])
        if err != nil {
            fmt.Println("Error writing to client:", err)
            break
        }
    }
}