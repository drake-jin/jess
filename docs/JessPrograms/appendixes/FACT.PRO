/* factorial */

domains
       i = integer
predicates
       factorial(i, i)
clauses
       factorial(0, 1):-!.
       factorial(F, Ans) :-
              F > 0,
              F_1 = F - 1,
              factorial(F_1, Cur_Ans),
              Ans = F * Cur_Ans.
              