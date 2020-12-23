'use strict'

/**
 * Calc current date send messages
 * @returns {string}
 */
function calcCurrentTime() {
    const date = new Date()

    const hours = date.getHours()
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

    return convertTime(hours, minutes, seconds)
}

function convertTime(hours, minutes, seconds) {
    return `[${hours}:${minutes}:${seconds}]`
}

/**
 * Create content in the DOM-element
 * @type {{string: (function(*=): Text), function: (function(*=): HTMLElementTagNameMap[K]), object: (function(*): *)}}
 */
const elementSelector = {
    'string' : child => document.createTextNode(child),
    'function' : child => document.createElement(child),
    'object' : child => child
}
/**
 *
 * @param message{string} user
 * @param nickname{string} user
 * @returns {object}
 */
export default function createMessage(message, nickname) {
    return node({
        type: 'div',
        classList: ['message'],
        children: [
            node({
                type: 'div',
                classList: ['user-name'],
                children: nickname + calcCurrentTime()
            }),
            node({
                type: 'div',
                classList: ['content'],
                children: message
            })
        ]
    })
}
/**
 * Return node DOM
 * @param option{object}
 */
function node(option) {
    const type = option.type? option.type : 'div'
    const element = document.createElement(type)

    if (option.children){
        if (option.children instanceof Array){
            option.children.forEach(child => element.appendChild(elementSelector[typeof child](child)))
        }
        else{
            element.appendChild(elementSelector[typeof option.children](option.children))
        }
    }

    if (option.href){
        element.href = option.href
    }
    if (option.value){
        element.value = option.value
    }
    if (option.selected){
        element.selected = option.selected
    }
    if (option.inputType){
        element.type = option.inputType
    }
    element.onclick = option.onclick ? (e) => option.onclick(e) : element.onclick;
    element.onload = option.onload ? option.onload : element.onload;

    if (typeof element.onload === "function") {
        element.onload();
    }

    if (option.classList) {
        option.classList.forEach(c => element.classList.add(c));
    }

    if (option.id) {
        element.id = option.id;
    }

    if (option.name) {
        element.name = option.name;
    }

    if (option.for) {
        element.htmlFor = option.for;
    }

    if (option.placeholder) {
        element.placeholder = option.placeholder;
    }

    if (option.disabled) {
        element.disabled = option.disabled;
    }

    if (option.styles) {
        const optionStyles = option.styles;
        const styles = element.style;
        if (optionStyles.background) {
            styles.background = optionStyles.background;
        }
        if (optionStyles.color) {
            styles.color = optionStyles.color;
        }
    }

    return element;
}
