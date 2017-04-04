              /* Program 5 */
domains
    person = symbol

predicates
    male(person)
    female(person)
    father(person,person)
    mother(person,person)
    sister(person,person)
    parent(person,person)
    brother(person,person)
    uncle(person,person)
    grandfather(person,person)
    curson(person,person)
    couple(person,person)
clauses
    male(bum).
    male(john).
    male(won).
    male(kon).
    male(hub).
    male(chul).
    
    
    
    female(jung).
    female(sunmi).
    female(tae).
    female(dahae).
    female(sunhee).
    female(mija).
  /*  female(beverly).
    female(fay).
    female(marilyn).
    female(sally). */

    mother(sunmi,jung).
    mother(tae,jung).
    mother(bum,jung).
    mother(kon,jung).
    mother(hub,jung).
    mother(chul,sunhee).
    mother(dahae,sunhee).
    mother(john,mija).
   
    father(bum,won).
    father(chul,hub).
    father(dahae,hub).
    father(hub,won).
    father(kon,won).
    father(sunmi,won).
    father(tae,won).    
    father(john,bum).
    

    parent(X,Y) if mother(X,Y).
    parent(X,Y) if father(X,Y).

    brother(X,Y) if       /*The brother of X is Y if  */
        male(Y) and       /*Y is a male and           */
        parent(X,P) and   /*the parent of X is P and  */
        parent(Y,P) and   /*the parent of Y is P and  */
        X <> Y.           /* X and Y are not the same */

    sister(X,Y) if        /*The sister of X is Y if   */
        female(Y) and     /*Y is female and           */
        parent(X,P) and   /*the parent of X is P and  */
        parent(Y,P) and   /*the parent of Y is P and  */
        X <> Y.           /*X and Y are not the same  */

    uncle(X,U) if         /*The uncle of X is U if    */
        mother(X,P) and   /*the mother of X is P and  */
        brother(P,U).     /*the brother of P is U.    */
    uncle(X,U) if         /*The uncle of X is U if    */
        father(X,P) and   /*the father of X is P and  */
        brother(P,U).     /*the brother of P is U     */

    grandfather(X,G) if   /*The grandfather of X is G */
        father(P,G) and   /*if the father of P is G   */
        mother(X,P).      /*and the mother of X is P. */
    grandfather(X,G) if   /*The grandfather of X is G */
        father(X,P) and   /*if the father of X is P   */
        father(P,G).      /*the father of P is G      */

    curson(X,Y) if parent(X,A) and 
                   parent(Y,B) and 
                   brother(A,B).
                   
    couple(X,Y) if parent(Z,X) and
                   parent(Z,Y) and 
                   X <> Y.