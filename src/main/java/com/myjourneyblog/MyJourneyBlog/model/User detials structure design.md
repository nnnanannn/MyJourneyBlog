User details structure design
├── User Class Fileds
│    ├── id
│    ├── username
│    ├── email
│    ├── password
│    ├── fullName
│    ├── bio
│    ├── profileImageUrl
│    ├── githubUsername
│    ├── linkedinUrl
│    ├── createdAt
│    ├── updatedAt
│    ├── enabled
│    ├── learningPosts
│    └── projectUpdates
├── User Class Methods
│    ├── onCreate
│    │   └── createdAt
│    │   └── updatedAt
│    └── onUpdate
│        └── updatedAt
├── UserDetails interface methods
│    └── GrantedAuthority implements getAuthorities
├── Check Methods (Boolean)
├── isAccountNonExpired
├── isAccountNonLocked
├── isCredentialsNonExpired
└── isEnabled