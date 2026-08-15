// Web to APK Interactive Script
document.addEventListener('DOMContentLoaded', () => {
    initClock();
    initTheme();
    initDeviceInfo();
    initCounter();
    initNotes();
});

// Live Clock
function initClock() {
    function update() {
        const now = new Date();
        const clockEl = document.getElementById('liveClock');
        const dateEl = document.getElementById('liveDate');
        
        if (clockEl) {
            clockEl.textContent = now.toLocaleTimeString('en-US', { hour12: false });
        }
        if (dateEl) {
            const options = { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric' };
            dateEl.textContent = now.toLocaleDateString('km-KH', options) || now.toLocaleDateString();
        }
    }
    update();
    setInterval(update, 1000);
}

// Theme Toggle
function initTheme() {
    const btn = document.getElementById('themeToggleBtn');
    const icon = document.getElementById('themeIcon');
    const savedTheme = localStorage.getItem('app_theme') || 'dark';

    applyTheme(savedTheme);

    btn.addEventListener('click', () => {
        const currentTheme = document.body.classList.contains('light-theme') ? 'light' : 'dark';
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        applyTheme(newTheme);
        localStorage.setItem('app_theme', newTheme);
        showNotification(newTheme === 'dark' ? '🌙 Dark Mode បានបើក' : '☀️ Light Mode បានបើក');
    });

    function applyTheme(theme) {
        if (theme === 'light') {
            document.body.classList.remove('dark-theme');
            document.body.classList.add('light-theme');
            icon.textContent = '☀️';
        } else {
            document.body.classList.remove('light-theme');
            document.body.classList.add('dark-theme');
            icon.textContent = '🌙';
        }
    }
}

// Device Info
function initDeviceInfo() {
    const infoEl = document.getElementById('screenInfo');
    if (infoEl) {
        infoEl.textContent = `${window.innerWidth} x ${window.innerHeight} px (${window.devicePixelRatio}x)`;
    }
}

// Counter Logic
let counter = 0;
function initCounter() {
    counter = parseInt(localStorage.getItem('app_counter') || '0', 10);
    renderCounter();
}

function updateCounter(delta) {
    counter += delta;
    localStorage.setItem('app_counter', counter);
    renderCounter();
}

function resetCounter() {
    counter = 0;
    localStorage.setItem('app_counter', counter);
    renderCounter();
    showNotification('🔄 បាន Reset លេខរាប់');
}

function renderCounter() {
    const el = document.getElementById('counterValue');
    if (el) el.textContent = counter;
}

// Local Notes Storage
let notes = [];
function initNotes() {
    const saved = localStorage.getItem('app_notes');
    if (saved) {
        try {
            notes = JSON.parse(saved);
        } catch (e) {
            notes = [];
        }
    } else {
        notes = ['ស្វាគមន៍មកកាន់ Web to APK 🎉', 'គាំទ្រ HTML, CSS, JS និង React/Vue Web Apps'];
    }
    renderNotes();

    const input = document.getElementById('noteInput');
    if (input) {
        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') addNote();
        });
    }
}

function addNote() {
    const input = document.getElementById('noteInput');
    if (!input) return;
    const text = input.value.trim();
    if (!text) {
        showNotification('⚠️ សូមបញ្ចូលអត្ថបទ');
        return;
    }
    notes.unshift(text);
    saveNotes();
    renderNotes();
    input.value = '';
    showNotification('✅ បានរក្សាទុកកំណត់ត្រា');
}

function deleteNote(index) {
    notes.splice(index, 1);
    saveNotes();
    renderNotes();
    showNotification('🗑️ បានលុបកំណត់ត្រា');
}

function saveNotes() {
    localStorage.setItem('app_notes', JSON.stringify(notes));
}

function renderNotes() {
    const list = document.getElementById('notesList');
    if (!list) return;
    list.innerHTML = '';
    if (notes.length === 0) {
        list.innerHTML = '<li class="note-item" style="color: var(--text-secondary); justify-content: center;">គ្មានកំណត់ត្រានៅឡើយទេ</li>';
        return;
    }
    notes.forEach((item, idx) => {
        const li = document.createElement('li');
        li.className = 'note-item';
        li.innerHTML = `
            <span>${escapeHtml(item)}</span>
            <button class="note-delete-btn" onclick="deleteNote(${idx})" title="Delete">✕</button>
        `;
        list.appendChild(li);
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Toast Notifications
let toastTimeout;
function showNotification(msg) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    toast.textContent = msg;
    toast.classList.add('show');
    clearTimeout(toastTimeout);
    toastTimeout = setTimeout(() => {
        toast.classList.remove('show');
    }, 2800);
}

function scrollToSection(id) {
    const el = document.getElementById(id);
    if (el) {
        el.scrollIntoView({ behavior: 'smooth' });
    }
}
