# Stripe Test Setup

Ce repo contient une intégration Stripe **en mode test uniquement** pour un flow de paiement simple et Stripe Connect (plateforme + providers).

## Variables d'environnement (Backend)

```bash
export STRIPE_SECRET_KEY=sk_test_...
export STRIPE_WEBHOOK_SECRET=whsec_...
export STRIPE_PUBLISHABLE_KEY=pk_test_...
export STRIPE_CURRENCY=eur
export STRIPE_DEFAULT_AMOUNT=30.00

export STRIPE_CONNECT_WEBHOOK_SECRET=whsec_...
export STRIPE_CONNECT_REFRESH_URL=http://localhost:4200/connect/refresh
export STRIPE_CONNECT_RETURN_URL=http://localhost:4200/connect/return
```

> ⚠️ Ne jamais committer de clés réelles. Utiliser uniquement des clés **test**.

## Lancer en local

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## Stripe CLI (webhooks)

### Paiements standards
```bash
stripe listen --forward-to localhost:8084/api/webhooks/stripe
```

### Stripe Connect (plateforme)
```bash
stripe listen --forward-to localhost:8084/api/payments-connect/webhooks/stripe
```

Copier les secrets `whsec_...` retournés par Stripe CLI dans `STRIPE_WEBHOOK_SECRET` et `STRIPE_CONNECT_WEBHOOK_SECRET`.

## Flow Paiement (standard)

1. Aller sur `http://localhost:4200/checkout`.
2. Saisir un `orderId` (correspond à un `bookingId` existant).
3. Utiliser une carte de test Stripe (ex: `4242 4242 4242 4242`, date future, CVC au hasard).
4. Vérifier:
   - PaymentIntent créé sur Stripe (test).
   - Transaction enregistrée en DB (table `payment_transactions`).
   - Webhook reçu et statut mis à jour (SUCCEEDED ou FAILED).

## Flow Stripe Connect (plateforme + provider)

### Onboarding provider
```bash
POST /api/payments-connect/providers/{providerId}/onboard
```
Retourne un `accountId` et un `url` d'onboarding Stripe. Ouvrir l’URL pour compléter le onboarding (test).

### Créer un PaymentIntent (charge client)
```bash
POST /api/payments-connect/intents
{
  "orderId": 123,
  "providerId": 45
}
```

### Webhook
Lorsque `payment_intent.succeeded` est reçu, un `Transfer` est créé vers le `connected account`.

## Notes / limitations
- Mode test uniquement (pas de débit réel).
- Le montant est calculé côté backend à partir du `bookingId` (provider basePrice si disponible, sinon valeur par défaut).
- Le status final est déterminé par webhook Stripe.
