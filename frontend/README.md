# HMS Frontend - Next.js Dashboard

Modern dashboard frontend for the Hall Management System built with Next.js 16, React 19, TypeScript, and Tailwind CSS 4.

## Requirements

- Node.js **20+**
- Backend API running at `http://localhost:8080` (see [../backend/README.md](../backend/README.md))

## Setup

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000).

## Authentication

Authentication is handled entirely by the backend API:

1. Login form sends `POST /api/auth/login` with email + password
2. Backend validates credentials and returns a JWT token + user profile
3. Token is stored in `localStorage` and attached as `Authorization: Bearer <token>` to all API requests
4. On 401 responses, the user is automatically logged out and redirected to the login page

### Demo Accounts

| Role | Email | Password |
|------|-------|----------|
| Student | student@hms.edu | student123 |
| Warden | warden@hms.edu | warden123 |
| Controlling Warden | cwarden@hms.edu | cwarden123 |
| Mess Manager | mess@hms.edu | mess123 |
| Clerk | clerk@hms.edu | clerk123 |
| HMC Chairman | hmc@hms.edu | hmc123 |

Click any demo account on the login page to auto-fill credentials.

## Project Structure

```
app/
├── page.tsx                    # Login page
├── layout.tsx                  # Root layout with AuthProvider
├── providers.tsx               # Context providers
├── globals.css                 # Tailwind CSS global styles
├── lib/
│   ├── api.ts                  # API client with JWT token management
│   ├── auth.tsx                # Auth context, role labels, demo accounts
│   ├── types.ts                # TypeScript interfaces
│   └── utils.ts                # Utility functions
├── components/
│   └── ui.tsx                  # Shared UI components
└── (dashboard)/                # Protected route group
    ├── layout.tsx              # Dashboard shell (sidebar, nav, auth guard)
    ├── page.tsx                # Role-based redirect
    ├── student/                # Student portal pages
    │   ├── page.tsx            # Dashboard
    │   ├── dues/               # View dues
    │   ├── complaints/         # File & view complaints
    │   └── payments/           # Payment history
    ├── warden/                 # Warden portal pages
    │   ├── page.tsx            # Dashboard
    │   ├── students/           # Student management
    │   ├── complaints/         # Complaints & ATR
    │   ├── occupancy/          # Room occupancy
    │   ├── staff/              # Staff management
    │   ├── salary/             # Salary sheets
    │   ├── expenditures/       # Expenditure tracking
    │   └── accounts/           # Annual financial statement
    ├── controlling-warden/     # Cross-hall oversight
    ├── mess-manager/           # Mess charge management
    ├── clerk/                  # Staff leave & salary
    └── hmc/                    # HMC Chairman admin
```

## Key Design Decisions

- **Client-side auth guard**: The `(dashboard)/layout.tsx` checks for an authenticated user and redirects to `/` if none found
- **Token auto-refresh**: On 401 responses, the API client clears the token and redirects to login
- **Role-based routing**: Each role has its own route prefix and sidebar navigation
- **No middleware**: Route protection is handled via React context + useEffect guards

## Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Production build |
| `npm run start` | Start production server |
| `npm run lint` | Run ESLint |

## Tech Stack

- **Next.js 16** with App Router
- **React 19** with Server Components
- **TypeScript 5**
- **Tailwind CSS 4** for styling
- **Lucide React** for icons
