"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth, ROLE_HOME } from "../lib/auth";
import { Spinner } from "../components/ui";

export default function DashboardRedirect() {
  const { user, isLoading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading) {
      if (user) {
        router.replace(ROLE_HOME[user.role]);
      } else {
        router.replace("/");
      }
    }
  }, [user, isLoading, router]);

  return <Spinner />;
}
