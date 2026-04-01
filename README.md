# 🚄 RailYatra — Railway Reservation System

A complete railway reservation web application built with **HTML, CSS, Java Servlets, and JSP**.

---

## 📁 Project Structure

```
railway-app/
├── pom.xml
└── src/main/
    ├── java/com/railway/
    │   ├── model/
    │   │   ├── Train.java          ← Train data + seat/WL logic
    │   │   └── Ticket.java         ← Ticket model
    │   ├── servlet/
    │   │   ├── SearchTrainsServlet.java
    │   │   ├── BookTicketServlet.java
    │   │   ├── TicketServlet.java
    │   │   └── CancelTicketServlet.java
    │   └── util/
    │       └── DataStore.java      ← Singleton in-memory store
    └── webapp/
        ├── index.html              ← Page 1: Search Trains
        ├── mybookings.html         ← Page 4: PNR Search
        ├── trainlist.jsp           ← Page 2: Train List
        ├── book.jsp                ← Page 3: Book Ticket
        ├── ticket.jsp              ← Ticket View + Cancel
        ├── css/
        │   └── style.css
        └── WEB-INF/
            └── web.xml
```

---

## 🚀 Setup Instructions

### Prerequisites
- **Java 11+**
- **Apache Maven 3.6+**
- **Apache Tomcat 9.x or 10.x**

---

### Option A: Build with Maven and Deploy to Tomcat

**Step 1 — Build the WAR file**
```bash
cd railway-app
mvn clean package
```
This creates: `target/railway.war`

**Step 2 — Deploy to Tomcat**
- Copy `target/railway.war` to `<TOMCAT_HOME>/webapps/`
- Start Tomcat: `<TOMCAT_HOME>/bin/startup.sh` (Linux/Mac) or `startup.bat` (Windows)
- Open browser: `http://localhost:8080/railway/`

---

### Option B: Use IntelliJ IDEA

1. Open IntelliJ → **File → Open** → select `railway-app` folder
2. IntelliJ detects the Maven project automatically
3. Add Tomcat server: **Run → Edit Configurations → + → Tomcat Server → Local**
4. In **Deployment** tab → click **+** → **Artifact** → select `railway:war exploded`
5. Set **Application context** to `/railway`
6. Click **Run** ▶

---

### Option C: Use Eclipse

1. **File → Import → Existing Maven Projects** → select `railway-app`
2. Right-click project → **Properties → Project Facets** → enable **Dynamic Web Module** + **Java**
3. Right-click project → **Run As → Run on Server** → choose Tomcat
4. Access: `http://localhost:8080/railway/`

---

## 📋 How to Use the Application

### 1. Search Trains (Home Page)
- Select **From** city (Bengaluru / Chennai / Hyderabad)
- Select **To** city (different from origin)
- Pick a **date**
- Click **Search Trains**

### 2. Train List Page
- See all trains for your route
- Each train card shows: Train name, number, timings, duration
- Class boxes show: **Price**, and either:
  - 🟢 `Available (N)` — N seats left
  - 🟠 `WL N` — Waiting list position N
  - 🔴 `Not Available` — Cannot book

### 3. Book a Ticket
- Click **Book Now** on any available/WL class
- Enter passenger name
- Choose class (pre-selected)
- Click **Confirm Booking**
- You get a **PNR number** — save it!

### 4. View Ticket / PNR Status
- Go to **My Bookings**
- Enter your PNR number
- See full ticket with status: CONFIRMED / WL1 / WL2...

### 5. Cancel Ticket
- View your ticket via PNR
- Click **Cancel Ticket**
- If you were CONFIRMED → first WL passenger auto-upgrades to CONFIRMED
- All remaining WL positions shift up (WL2→WL1, WL3→WL2, etc.)

---

## 🧠 Core Data Structures Used

| Structure | Usage |
|-----------|-------|
| `HashMap<String, Ticket>` | Maps PNR → Ticket (O(1) lookup) |
| `Queue<String>` (ArrayDeque) | Waiting list per class per train (FIFO) |
| `ArrayList<Train>` | All trains list |
| `HashMap<String, Train>` | Train number → Train (O(1) lookup) |
| `HashMap<String, Integer>` | Confirmed seats count per class |

---

## 🚂 Hardcoded Train Data

| Route | Train | Number |
|-------|-------|--------|
| Bengaluru → Chennai | Vande Bharat Express | 20608, 20663 |
| Bengaluru → Chennai | Shatabdi Express | 12028, 12008 |
| Bengaluru → Hyderabad | KCG Vande Bharat | 20704 |
| Bengaluru → Hyderabad | Rajdhani Express | 22691 |
| Bengaluru → Hyderabad | Duronto Express | 12213 |
| Chennai → Bengaluru | Vande Bharat Express | 20607 |
| Chennai → Bengaluru | Shatabdi Express | 12027 |
| Chennai → Hyderabad | Chennai Express | 12603 |
| Chennai → Hyderabad | Charminar Express | 12759 |
| Hyderabad → Bengaluru | KCG Vande Bharat | 20703 |
| Hyderabad → Bengaluru | Rajdhani Express | 22692 |
| Hyderabad → Chennai | Charminar Express | 12760 |

---

## 🎯 Booking Logic (Queue-Based)

```
BOOKING:
  if confirmedSeats < 10:
      confirmedSeats++
      status = "CONFIRMED"
  else:
      waitingList.add(pnr)   ← add to end of queue
      status = "WL" + waitingList.size()

CANCELLATION (CONFIRMED ticket):
  confirmedSeats--
  if waitingList not empty:
      promotedPnr = waitingList.poll()   ← FIFO: remove from front
      tickets[promotedPnr].status = "CONFIRMED"
      // Re-number remaining: WL2→WL1, WL3→WL2, etc.
      for (pos, pnr) in enumerate(waitingList, 1):
          tickets[pnr].status = "WL" + pos

CANCELLATION (WL ticket):
  remove pnr from waitingList
  re-number remaining WL tickets
```

---

## ⚠️ Notes

- **Data is in-memory only** — restarting Tomcat clears all bookings.
  (For persistence, add a database like H2 or MySQL)
- Each train class has **10 total seats** (configurable in `Train.java`: `TOTAL_SEATS`)
- PNR numbers are randomly generated (13 chars: `PNR` + 10 digits)
- The app uses **no external frameworks** — pure Servlets + JSP

---

## 🎨 Tech Stack

- **Frontend**: HTML5, CSS3 (custom), Google Fonts (Syne + DM Sans)
- **Backend**: Java Servlets (javax.servlet)
- **Views**: JSP (JavaServer Pages)
- **Server**: Apache Tomcat 9/10
- **Build**: Maven

---

*Built for learning Java Servlets, Queues, and Web Development fundamentals.*
