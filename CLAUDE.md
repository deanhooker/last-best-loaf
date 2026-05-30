# Last Best Loaf Bakery — Claude Context

## Project Overview
A web app for a micro bakery in Bozeman, MT. Customers browse bake-day events, add items to a cart, and place orders for pickup or delivery. The bakery's "Community Loaf" initiative gives one reduced-price loaf for every full-price purchase.

## Tech Stack
- **Frontend**: ClojureScript, Reagent (React wrapper), re-frame (Redux-like state), reitit (routing), shadow-cljs build
- **Backend**: Clojure, Ring/Jetty (HTTP), reitit (routing), next.jdbc, PostgreSQL, Malli (schema validation), Migratus (migrations)
- **Shared**: `.cljc` files in `shared/src/bakery/schema/` used by both frontend and backend
- **Styling**: Inline styles only (no CSS framework)

## Repo Structure
```
last-best-loaf/
  backend/
    deps.edn                      # Clojure deps (port 3000)
    src/bakery/
      server.clj                  # Jetty start!/stop!
      http.clj                    # Ring router + handlers + CORS
      db.clj                      # next.jdbc datasource, migrations
      products.clj                # CRUD + seed data
      bake_day.clj                # CRUD + seed data
      bake_day_products.clj       # CRUD + seed data
      event.clj                   # Assembles bake_day + products into event response
    resources/migrations/         # SQL migration files (migratus)
  frontend/
    shadow-cljs.edn               # shadow-cljs config (port 8000)
    src/bakery/
      main.cljs                   # Entry point: init + start
      router.cljs                 # reitit-frontend routes
      db.cljs                     # re-frame default-db (app state shape)
      events.cljs                 # re-frame events + effects
      subs.cljs                   # re-frame subscriptions
      views.cljs                  # Reagent components
      http.cljs                   # fetch-json / fetch-edn helpers
      validation.cljs             # Checkout form validation
      images.cljs                 # Hero image list
  shared/
    src/bakery/schema/
      product.cljc
      bake_day.cljc
      bake_day_products.cljc
  project.org                     # Feature spec and data model doc
```

## Database Schema
**products**: id, name, price (cents bigint), description  
**bake_days**: id, date (unique), name  
**bake_day_products**: id, bake_day_id, product_id, inventory, sold_count, available, price_override (cents)

Prices are stored and passed as **integer cents** (e.g. $10.00 = 1000). `format-money` in `frontend/src/bakery/util.cljs` handles display.

## Frontend State Shape (re-frame db)
```clojure
{:route           nil          ; current reitit match
 :products        []           ; flat list from /api/products
 :cart            {:event-id nil, :items {item-id {:item item, :qty n}}}
 :checkout        {:fulfillment :pickup, :pickup-date "", :pickup-window "",
                   :delivery-address "", :notes ""}
 :customer        {:name "", :email "", :phone ""}
 :events          {id event}   ; keyed by id, fetched on demand
 :events-list     nil          ; list from /api/bake-days
 :ui              {:errors {}, :submitting? false}
 :hero/current-index 0}
```

## API Routes (backend)
| Method | Path | Handler |
|--------|------|---------|
| GET | /ping | ping |
| GET | /health | health (checks DB) |
| GET | /api/products | list all products |
| GET | /api/bake-days | list all bake days |
| GET | /api/bake-day-products/:id | products for a bake day |
| GET | /api/event/:id | full event (bake day + merged products) |
| POST | /api/order | **not yet implemented** |

CORS is open to `http://localhost:8000` only (dev). Production CORS needs updating before deploy.

## Frontend Routes
| Path | Route name | View |
|------|------------|------|
| / | :home-page | hero (image reel) |
| /menu | :menu | coming-soon stub |
| /about | :about | coming-soon stub |
| /cart | :cart | cart |
| /checkout | :checkout | checkout form |
| /contact | :contact | contact info |
| /event/:event-id | :event | event-page (products for a bake day) |

## Key Patterns
- **re-frame events** follow: `reg-event-fx` for side effects (HTTP fetches), `reg-event-db` for pure state updates.
- **HTTP effects** are custom `reg-fx` — each fetch has a matching `:fetch-X` effect and `:set-X` event.
- **Navigation** uses `:navigate!` event → `:navigate` effect → `rfe/push-state`. Route changes trigger `:navigated` which may dispatch data loads (e.g. `:load-event` on `:event` route).
- **Cart constraint**: items from different bake days cannot be mixed — enforced in `:cart/add`.
- **Validation** runs client-side in `validation.cljs`; `:validate-checkout` stores errors in `[:ui :errors]`.

## What's Not Yet Built
- POST /api/order handler (backend + frontend submission flow)
- Order idempotency, inventory concurrency control
- Confirmation page
- Admin view
- Delivery radius check (Google Maps)
- Bake day cutoff time enforcement
- Confirmation email
- Menu page (currently shows "coming soon")
- About page
- Production CORS / hosting / environment config
- Payment

## Dev Setup
- Backend: `cd backend && clj -M:dev` then `(bakery.server/start!)` in REPL
- Frontend: `cd frontend && npx shadow-cljs watch app` → http://localhost:8000
- DB: PostgreSQL, dbname/user/password all `bakery`
