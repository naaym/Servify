# Stripe Test Setup

Ce repo contient une intégration Stripe **en mode test uniquement** pour un flow de paiement simple.

## Variables d'environnement (Backend)

```bash
export STRIPE_SECRET_KEY=sk_test_...
export STRIPE_WEBHOOK_SECRET=whsec_...
export STRIPE_PUBLISHABLE_KEY=pk_test_...
export STRIPE_CURRENCY=eur
export STRIPE_DEFAULT_AMOUNT=30.00

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

Copier le secret `whsec_...` retourné par Stripe CLI dans `STRIPE_WEBHOOK_SECRET`.

## Flow Paiement (standard)

1. Aller sur `http://localhost:4200/checkout`.
2. Saisir un `orderId` (correspond à un `bookingId` existant).
3. Utiliser une carte de test Stripe (ex: `4242 4242 4242 4242`, date future, CVC au hasard).
4. Vérifier:
   - PaymentIntent créé sur Stripe (test).
   - Transaction enregistrée en DB (table `payment_transactions`).
   - Webhook reçu et statut mis à jour (SUCCEEDED ou FAILED).

## Notes / limitations
- Mode test uniquement (pas de débit réel).
- Le montant est calculé côté backend à partir du `bookingId` (provider basePrice si disponible, sinon valeur par défaut).
- Le status final est déterminé par webhook Stripe.
