'use strict'

import './css/layout.css'
import './css/header.css'
import './css/main.css'
import './css/footer.css'

import UserLogo from '../public/img/logo2.jpg'

import createMessage from './js/utils'

/**
 * Roles users
 * @type {{}}
 */
const roles = {
    'admin': 0,
    'user': 1
}

const defaultURL = 'ws://192.168.43.162:3443/chat'
const urls = {
    "authorization" : "auth",
    "registration" : "record",
}
/**
 * Displays the desired block
 * @param visibleBlock{String} block to be displayed
 * @param otherBlocks{Array}
 */
function exposeBlock(visibleBlock, otherBlocks) {
    document.querySelector(visibleBlock).style.display = 'flex'
    otherBlocks.forEach(classNameBlock => {
        document.querySelector(classNameBlock).style.display = 'none'
    })
}
/**
 * Authorization user
 */
function authorization() {
    const data = JSON.stringify({
        nickname: document.getElementById('input-ncknm').value.toString(),
        password: document.getElementById('input-pswrd').value.toString(),
    })
    const headers = new Headers({
        'Access-Control-Allow-Origin': 'http://localhost:3443',
    })
    fetch('http://localhost:3443/chat/authorization/' + urls["authorization"], {
        method: 'POST',
        body: data,
        headers: headers
    })
        .then(response => response.json())
        .then(response => {
            if (response.hasOwnProperty('nickname')){
                console.log(`User with nickname ${response.nickname} successfully authorization`)
                console.log(`You can start communication`)
                exposeBlock('.user-info', ['.main-window', '.authorization', '.registration'])
                document.getElementById('nick').innerHTML = response.nickname

                if (response.role === roles['admin']){
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'flex'
                }
                else{
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'none'
                }
                document.getElementById('send-msg').style.display = 'flex'
            }
            else{
                console.log(`Error authorization, incorrect nickname or password`)
            }
        })
}
/**
 * Function for registration user
 */
function registration() {
    const data = JSON.stringify({
        fullName: document.getElementById('input-fullName-r').value.toString(),
        email: document.getElementById('input-email-r').value.toString(),
        nickname: document.getElementById('input-ncknm-r').value.toString(),
        password: document.getElementById('input-pswrd-r').value.toString()
    })

    fetch('http://192.168.43.162:3443/chat/authorization/' + urls["registration"], {
        method: 'POST',
        body: data,
    })
        .then(response => response.json())
        .then(response => {
            console.log(`User with nickname ${data.nickname} successfully authorization`)
            console.log(`You can start communication`)
            exposeBlock('.user-info', ['.main-window', '.authorization', '.registration'])
            document.getElementById('nick').innerHTML = response.nickname

            if (response.role === roles['admin']){
                document.querySelector('.btn-check-your-msg').style.display = 'flex'
                document.querySelector('.check-user-messages').style.display = 'flex'
            } else{
                document.querySelector('.btn-check-your-msg').style.display = 'flex'
                document.querySelector('.check-user-messages').style.display = 'none'
            }
            document.getElementById('send-msg').style.display = 'flex'
        })
}

function exit() {
    const data = JSON.stringify({
        header: "exit",
    })
    socket.send(data)

    exposeBlock('.main-window', ['.registration','.authorization','.user-info'])
}

const socket = new WebSocket(defaultURL)

socket.onopen = () => {

}
socket.onmessage = (event) => {
    const answerServer = JSON.parse(event.data)
    document.querySelector('.show-msg').appendChild(createMessage(answerServer.message, answerServer.nickname))
}
socket.onerror = () => {
    console.error(`Error`)
}
socket.onclose = () => {
    console.log('Connection with server is closed')
}
/**
 * Send message to server
 */
function sendMessage(){
    const message = document.getElementById('message').value.toString()
    document.getElementById('message').innerHTML = ""
    const nickname = document.getElementById('nick').innerText.toString()
    const data = JSON.stringify({
        message: message,
        nickname: nickname
    })
    socket.send(data)
}

function allOnclick(){
    document.getElementById('send-msg').onclick = () => {
        sendMessage()
    }
    document.getElementById('btn-auth').onclick = () => {
        authorization()
    }
    document.getElementById('btn-back-a').onclick = () => {
        exposeBlock('.main-window', ['.user-info','.authorization','.registration'])
    }
    document.getElementById('btn-back-r').onclick = () => {
        exposeBlock('.main-window', ['.user-info','.authorization','.registration'])
    }
    document.getElementById('btn-auth-r').onclick = () => {
        registration()
    }
    document.getElementById('btn-exit').onclick = () => {
        exit()
    }
    document.getElementById('btn-check-all').onclick = () => {

    }
    document.getElementById('btn-check-your').onclick = () => {

    }
    document.getElementById('btn-window-registration').onclick = () => {
        exposeBlock('.registration', ['.main-window','.user-info','.authorization'])
    }
    document.getElementById('btn-window-authorization').onclick = () => {
        exposeBlock('.authorization', ['.main-window','.user-info','.registration'])
    }
}
allOnclick()
