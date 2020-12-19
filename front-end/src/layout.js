/**
 * Roles users
 * @type {{}}
 */
const roles = {
    'admin': 1,
    'user': 0
}
const defaultURL = 'wss://192.168.43.162:4334/'
/**
 *
 * @type {{authorization: string, exit: string, registration: string, send: string}}
 */
const urls = {
    'registration' : 'record',
    'authorization' : 'auth',
    'send' : 'chat',
    'exit' : 'close'
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
    const data = {
        nickname: document.getElementById('input-ncknm').value.toString(),
        password: document.getElementById('input-pswrd').value.toString(),
    }
    fetch(defaultURL + urls['authorization'], {
        method: 'POST',
        body: data
    })
        .then(response => response.json())
        .then(response => {
            if (response.check === true){
                console.log(`User with nickname ${data.nickname} successfully authorization`)
                console.log(`You can start communication`)
                exposeBlock('.user-info', ['.main-window', '.authorization', '.registration'])
                document.querySelectorAll('.nickname').value = data.nickname

                if (response.role === roles['admin']){
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'flex'
                }
                else{
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'none'
                }
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
    const data = {
        fullName: document.getElementById('input-fullName-r').value.toString(),
        email: document.getElementById('input-email-r').value.toString(),
        nickname: document.getElementById('input-ncknm-r').value.toString(),
        password: document.getElementById('input-pswrd-r').value.toString()
    }
    fetch(defaultURL + urls['registration'], {
        method: 'POST',
        body: data
    })
        .then(response => response.json())
        .then(response => {
            if (response.check === true){
                console.log(`User with nickname ${data.nickname} successfully authorization`)
                console.log(`You can start communication`)
                exposeBlock('.user-info', ['.main-window', '.authorization', '.registration'])
                document.querySelectorAll('.nickname').value = data.nickname

                if (response.role === roles['admin']){
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'flex'
                } else{
                    document.querySelector('.btn-check-your-msg').style.display = 'flex'
                    document.querySelector('.check-user-messages').style.display = 'none'
                }
            } else{
                console.log(`Error authorization, incorrect nickname or password`)
            }
        })
}

function exit() {
    fetch(defaultURL + urls['exit'])
        .then(response => console.log('Connection with server successfully closed'))
    exposeBlock('.main-window', ['.registration','.authorization','.user-info'])
}

const socket = new WebSocket(defaultURL)

socket.onopen = () => {
    console.log("Connected with server successfully")
}
socket.onmessage = (event) => {
    const data = JSON.stringify(event.data)
    const message = data.message
    const nickname = data.nickname
    document.querySelector('.show-msg').appendChild(createMessage(message, nickname))
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
    socket.send(message)
}

